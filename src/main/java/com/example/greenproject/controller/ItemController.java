package com.example.greenproject.controller;

import com.example.greenproject.dto.req.CreateCartItemRequest;
import com.example.greenproject.dto.req.UpdateCartQuantity;
import com.example.greenproject.dto.res.BaseResponse;
import com.example.greenproject.dto.res.DataResponse;
import com.example.greenproject.service.ItemService;
import com.example.greenproject.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    @PostMapping("/add-cart")
    public ResponseEntity<?> addItemToCart(@RequestBody CreateCartItemRequest createCartItemRequest){
        return ResponseEntity.ok().body(new DataResponse(
                HttpStatus.OK.value(),
                Constants.SUCCESS_MESSAGE,
                itemService.createCartItem(createCartItemRequest)
        ));
    }
    @GetMapping("/my-cart")
    public ResponseEntity<?> getMyCart(){
        return ResponseEntity.ok().body(new DataResponse(
                HttpStatus.OK.value(),
                Constants.SUCCESS_MESSAGE,
                itemService.getAllCartItem()
        ));
    }

    @PutMapping("/update-cart/{itemId}")
    public ResponseEntity<?> updateMyCart(@RequestBody UpdateCartQuantity updateCartQuantity, @PathVariable Long itemId){
        return ResponseEntity.ok().body(new DataResponse(
                HttpStatus.OK.value(),
                Constants.SUCCESS_MESSAGE,
                itemService.updateCartQuantity(updateCartQuantity,itemId)
        ));
    }

    @DeleteMapping("/delete-cart/{itemId}")
    public ResponseEntity<?> deleteCartItem(@PathVariable Long itemId){
        itemService.deleteItemById(itemId);
        return ResponseEntity.ok().body(new BaseResponse(
                HttpStatus.OK.value(),
                Constants.SUCCESS_MESSAGE
        ));
    }
}
