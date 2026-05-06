package org.tama.tamaapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.tama.sharelib.common.dto.SimpleResponse;
import org.tama.sharelib.common.util.UploadFile;
import org.tama.tamaapi.config.aspect.PreAuthentication;

import org.tama.tamaapi.domain.item.ColorItem;
import org.tama.tamaapi.domain.item.ColorItemImage;
import org.tama.tamaapi.query.item.ColorItemQueryRepository;
import org.tama.tamaapi.command.item.ItemService;
import org.tama.tamaapi.command.S3Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.tama.tamaapi.dto.requestDto.item.save.SaveColorItemImageRequest;
import org.tama.tamaapi.dto.requestDto.item.save.SaveColorItemImageWrapperRequest;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequiredArgsConstructor
public class UploadApiController {

    private final ColorItemQueryRepository colorItemQueryRepository;
    private final ItemService itemService;
    private final S3Service s3Service;

    @PostMapping("/api/items/images/new")
    @PreAuthentication
    @Secured("ROLE_ADMIN")
    public ResponseEntity<SimpleResponse> saveItemImages(@Valid @ModelAttribute SaveColorItemImageWrapperRequest wrapperRequest) {
        //이미지 파일인지 검증
        wrapperRequest.getRequests().forEach(req -> s3Service.areFilesImage(req.getFiles()));

        List<Long> colorItemIds = wrapperRequest.getRequests().stream().map(SaveColorItemImageRequest::getColorItemId).toList();
        List<ColorItem> colorItems = colorItemQueryRepository.findAllById(colorItemIds);

        //colorItemImages 엔티티 생성
        Map<Long, List<UploadFile>> uploadFileMap = wrapperRequest.getRequests().stream()
                .collect(Collectors.toMap(
                        SaveColorItemImageRequest::getColorItemId,
                        ci -> {
                            List<MultipartFile> files = ci.getFiles();
                            return s3Service.storeFiles(files);
                        }
                ));

        List<ColorItemImage> colorItemImages = colorItems.stream()
                .flatMap(ci -> {
                    List<UploadFile> uploadFiles = uploadFileMap.get(ci.getId());
                    return IntStream.range(0, uploadFiles.size())  // 인덱스를 생성
                            .mapToObj(i -> new ColorItemImage(ci, uploadFiles.get(i), i + 1)); // 1부터 시작하는 순서
                })
                .toList();

        itemService.saveColorItemImages(colorItemImages);
        return ResponseEntity.status(HttpStatus.OK).body(new SimpleResponse("저장 성공"));
    }


}
