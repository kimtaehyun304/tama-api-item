package org.example.tamaapi.controller;

import lombok.RequiredArgsConstructor;
import org.example.tamaapi.command.item.ItemService;
import org.example.tamaapi.common.aspect.InternalOnly;
import org.example.tamaapi.dto.feign.requestDto.ItemOrderCountRequest;
import org.example.tamaapi.dto.feign.responseDto.*;
import org.example.tamaapi.query.DecreaseStockLogQueryRepository;
import org.example.tamaapi.query.item.service.ItemQueryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@InternalOnly
public class FeignApiController {

    private final ItemQueryService itemQueryService;
    private final ItemService itemService;
    private final DecreaseStockLogQueryRepository decreaseStockLogQueryRepository;

    //-----from 주문 msa-----
    @GetMapping("/api/items/totalPrice")
    public int getTotalPrice(@RequestBody List<ItemOrderCountRequest> requests) {
        return itemQueryService.getItemsTotalPrice(requests);
    }

    //주문 아이템 생성시 필요
    @GetMapping("/api/items/price")
    public List<ItemPriceFeignResponse> getItemsPrice(@RequestParam List<Long> colorItemSizeStockIds) {
        return itemQueryService.getItemsPrice(colorItemSizeStockIds);
    }

    @PutMapping("/api/items/stocks/increase")
    public void increaseStocks(@RequestBody List<ItemOrderCountRequest> requests) {
        itemService.increaseStocks(requests);
    }

    @PutMapping("/api/items/stocks/decrease")
    public void decreaseStocks(@RequestBody List<ItemOrderCountRequest> requests, @RequestParam String uuid) {
        itemService.decreaseStocks(requests, uuid);
    }

    //-----from 읽기 msa-----
    @GetMapping("/api/items/{itemId}")
    public ItemSyncResponse getItem(@PathVariable Long itemId) {
        return itemQueryService.createItemSyncResponse(itemId);
    }

    @GetMapping("/api/items/stock/decrease/log/exist")
    boolean existDecreaseStockLog(@RequestParam String paymentId){
        return decreaseStockLogQueryRepository.existsByPaymentId(paymentId);
    }

    /*
    @GetMapping("/api/items/stock/decrease/log/recent")
    List<DecreaseStockLog> findRecentDecreaseStockLogs(int hours){
        LocalDateTime localDateTime = LocalDateTime.now().minusHours(hours);
        return decreaseStockLogRepository.findByCreatedAtAfter(localDateTime);
    }
    */

    @DeleteMapping("/api/items/stock/decrease/log")
    void deleteDecreaseStockLog(@RequestParam String paymentId){
        itemService.deleteDecreaseStockLog(paymentId);
    }



}