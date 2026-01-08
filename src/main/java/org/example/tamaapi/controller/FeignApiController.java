package org.example.tamaapi.controller;

import lombok.RequiredArgsConstructor;
import org.example.tamaapi.command.item.ItemService;
import org.example.tamaapi.common.aspect.InternalOnly;
import org.example.tamaapi.common.util.ErrorMessageUtil;
import org.example.tamaapi.domain.item.ColorItem;
import org.example.tamaapi.domain.item.ColorItemImage;
import org.example.tamaapi.domain.item.ColorItemSizeStock;
import org.example.tamaapi.domain.item.Item;
import org.example.tamaapi.dto.feign.requestDto.ItemOrderCountRequest;
import org.example.tamaapi.dto.feign.requestDto.ItemOrderCountRequestWrapper;
import org.example.tamaapi.dto.feign.responseDto.*;
import org.example.tamaapi.query.item.ColorItemImageQueryRepository;
import org.example.tamaapi.query.item.ColorItemSizeStockQueryRepository;
import org.example.tamaapi.query.item.ItemQueryRepository;
import org.example.tamaapi.query.item.service.ItemQueryService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@InternalOnly
public class FeignApiController {

    private final ItemQueryService itemQueryService;
    private final ItemService itemService;

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
    public void decreaseStocks(@RequestBody List<ItemOrderCountRequest> requests) {
        itemService.decreaseStocks(requests);
    }

    //-----from 읽기 msa-----
    @GetMapping("/api/items/{itemId}")
    public ItemSyncResponse getItem(@PathVariable Long itemId) {
        return itemQueryService.createItemSyncResponse(itemId);
    }

}
