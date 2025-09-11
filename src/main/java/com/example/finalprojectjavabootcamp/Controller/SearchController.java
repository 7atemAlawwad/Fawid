package com.example.finalprojectjavabootcamp.Controller;

import com.example.finalprojectjavabootcamp.Api.ApiResponse;
import com.example.finalprojectjavabootcamp.DTOIN.SearchCarDTOIn;
import com.example.finalprojectjavabootcamp.DTOIN.SearchRealEstateDTOIn;
import com.example.finalprojectjavabootcamp.Model.User;
import com.example.finalprojectjavabootcamp.Service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @PostMapping("/create-car-search")
    public ResponseEntity<?> createCarSearch(@AuthenticationPrincipal User user, @RequestBody SearchCarDTOIn searchCarDTOIn) {
        searchService.CreateCarSearch(searchCarDTOIn, user.getId());
        return ResponseEntity.status(200).body(new ApiResponse("Car search created successfully"));
    }

    @PostMapping("/create-real-estate-search")
    public ResponseEntity<?> createRealEstateSearch(@AuthenticationPrincipal User user,@RequestBody SearchRealEstateDTOIn searchRealEstateDTOIn) {
        searchService.CreateRealEstateSearch(searchRealEstateDTOIn, user.getId());
        return ResponseEntity.status(200).body(new ApiResponse("Real estate search created successfully"));
    }

    @GetMapping("/get-results/{searchId}")
    public ResponseEntity<?> getSearchResults(@AuthenticationPrincipal User user,@PathVariable Integer searchId) {
        return ResponseEntity.ok(searchService.getSearchResultsById(searchId, user.getId()));
    }

}