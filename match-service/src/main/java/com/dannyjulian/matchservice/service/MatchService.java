package com.dannyjulian.matchservice.service;

import com.dannyjulian.matchservice.dto.MatchRequest;
import com.dannyjulian.matchservice.dto.MatchResponse;
import com.dannyjulian.matchservice.model.MatchItem;
import com.dannyjulian.matchservice.repository.MatchRepository;
import com.dannyjulian.matchservice.util.BidOrAsk;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MatchService {

    private final MatchRepository matchRepository;

    public MatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    public List<MatchRequest> getMatchesByGuid(String guid) {
        return matchRepository.findAllByGuid(guid).stream()
                .map(this::getMatchRequest).toList();
    }

    public MatchResponse doMatch(MatchRequest matchRequest) {
        MatchItem matchItem = getMatchItem(matchRequest);
        matchRepository.save(matchItem);

        switch (matchItem.getBidOrAsk()) {
            case ASK -> matchBid(matchItem);
            case BID -> matchAsk(matchItem);
        }

        return MatchResponse.builder()
                .matchSucceeded(false)
                .build();
    }

    // If its a BID(BUY):
    //- Search for all ASKS for the same GUID where quantity >=0
    //- Find the closest ask by price
    //- substract quantities (ask qty must not be negative)
    //- If ask qty is not enough to supply the bid, find the next closest ask and repeat the process.

    private String matchBid(MatchItem matchItem) {
        String result = "Not matched";

        List<MatchItem> matchItemList = matchRepository.findAllByGuid(matchItem.getGuid());
        matchItemList = matchItemList.stream().filter(i -> i.getBidOrAsk() == matchItem.getBidOrAsk()).collect(Collectors.toList());
        if (!matchItemList.isEmpty()) {
            MatchItem closestItem = getClosestByPrice(matchItemList, matchItem.getPrice());
            substractQty(closestItem, matchItem);  // Before doing the substraction get all the required items and do the substraction all at once in one transaction
        }

        return result;
    }

    private String matchAsk(MatchItem matchItem) {
        return "";
    }

    private MatchItem getClosestByPrice(List<MatchItem> matchItemList, BigDecimal price) {
        MatchItem closest = matchItemList.get(0);

        for (MatchItem item: matchItemList) {
            if (Math.abs(item.getPrice().subtract(price).doubleValue()) < Math.abs(closest.getPrice().subtract(price).doubleValue())) {
                closest = item;
            }
        }

        return closest;
    }

    private boolean substractQty(MatchItem item1, MatchItem item2) {
        if (item1.getQuantity() >= item2.getQuantity()) {
            item1.setQuantity(item1.getQuantity() - item2.getQuantity());
        } else {
            item1.setQuantity(0);
        }
        return false;
    }


    private MatchItem getMatchItem(MatchRequest matchRequest) {
        var item = new MatchItem();
        item.setGuid(matchRequest.getGuid());
        item.setPrice(matchRequest.getPrice());
        item.setQuantity(matchRequest.getQuantity());
        item.setBidOrAsk(matchRequest.getBidOrAsk());
        return item;
    }

    private MatchRequest getMatchRequest(MatchItem matchItem) {
        var matchRequest = new MatchRequest();
        matchRequest.setGuid(matchItem.getGuid());
        matchRequest.setPrice(matchItem.getPrice());
        matchRequest.setQuantity(matchItem.getQuantity());
        matchRequest.setBidOrAsk(matchItem.getBidOrAsk());
        return matchRequest;
    }

}
