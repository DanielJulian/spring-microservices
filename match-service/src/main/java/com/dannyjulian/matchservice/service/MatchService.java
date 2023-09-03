package com.dannyjulian.matchservice.service;

import com.dannyjulian.matchservice.dto.MatchRequest;
import com.dannyjulian.matchservice.dto.MatchResponse;
import com.dannyjulian.matchservice.model.MatchItem;
import com.dannyjulian.matchservice.repository.MatchRepository;

import com.dannyjulian.matchservice.util.BidOrAsk;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class MatchService {

    private final MatchRepository matchRepository;

    public MatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    @Transactional(readOnly = true)
    public List<MatchRequest> getMatchesByGuid(String guid) {
        return matchRepository.findAllByGuid(guid).stream()
                .map(this::getMatchRequest).toList();
    }


    @Transactional
    public MatchResponse doMatch(MatchRequest matchRequest) {
        MatchItem matchItem = getMatchItem(matchRequest);
        matchRepository.save(matchItem);

        List<MatchItem> matchedItems = switch (matchItem.getBidOrAsk()) {
            case ASK -> matchAsk(matchItem);
            case BID -> matchBid(matchItem);
        };

        log.info("Matched items: {}", matchedItems);
        boolean matched = !matchedItems.isEmpty();

        return MatchResponse.builder()
                .matchSucceeded(matched)
                .bidOrAsk(matchItem.getBidOrAsk())
                .price(matchItem.getPrice())
                .quantity(matchItem.getQuantity())
                .guid(matchItem.getGuid())
                .build();
    }

    // If its a BID(BUY):
    //- Search for all ASKS for the same GUID where quantity >=0
    //- Find the closest ask by price
    //- substract quantities (ask qty must not be negative)
    //- If ask qty is not enough to supply the bid, find the next closest ask and repeat the process.

    private List<MatchItem> matchBid(MatchItem matchItem) {
        List<MatchItem> matchedItems = new ArrayList<>();

        List<MatchItem> itemsWithSameGuid = matchRepository.findAllByGuid(matchItem.getGuid()); // TODO add quantity > 0 and Only ASK filter
        List<MatchItem> askItems = new ArrayList<>(itemsWithSameGuid.stream().filter(i -> i.getBidOrAsk() == BidOrAsk.ASK).filter(i -> i.getQuantity() > 0).toList());

        if (!askItems.isEmpty()) {
            while (matchItem.getQuantity() >= 0 && !askItems.isEmpty()) {
                MatchItem closestItem = getClosestByPrice(askItems, matchItem.getPrice());
                subtractQty(closestItem, matchItem);
                matchedItems.add(closestItem);
                askItems.remove(closestItem);
            }
        }

        return matchedItems;
    }

    // If its an ASK(SELL):
    //- Search for all BIDs for the same GUID where quantity >=0
    //- Find the closest bid by price
    //- substract quantities (bid qty must not be negative)
    //- If bid qty is not enough to supply the ask, find the next closest bid and repeat the process.
    private List<MatchItem> matchAsk(MatchItem matchItem) {
        List<MatchItem> matchedItems = new ArrayList<>();

        List<MatchItem> itemsWithSameGuid = matchRepository.findAllByGuid(matchItem.getGuid()); // TODO add quantity > 0 and Only ASK filter
        List<MatchItem> bidItems = new ArrayList<>(itemsWithSameGuid.stream().filter(i -> i.getBidOrAsk() == BidOrAsk.BID).filter(i -> i.getQuantity() > 0).toList());

        if (!bidItems.isEmpty()) {
            while (matchItem.getQuantity() >= 0 && !bidItems.isEmpty()) {
                MatchItem closestItem = getClosestByPrice(bidItems, matchItem.getPrice());
                subtractQty(closestItem, matchItem);
                matchedItems.add(closestItem);
                bidItems.remove(closestItem);
            }
        }

        return matchedItems;
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

    private void subtractQty(MatchItem item1, MatchItem item2) {
        int item1Qty = item1.getQuantity();
        int item2Qty = item2.getQuantity();
        if (item1Qty >= item2Qty) {
            item1.setQuantity(item1.getQuantity() - item2.getQuantity());
            item2.setQuantity(0);
        } else {
            item1.setQuantity(0);
            item2.setQuantity(item2.getQuantity() - item1.getQuantity());
        }
        matchRepository.save(item1);
        matchRepository.save(item2);
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
