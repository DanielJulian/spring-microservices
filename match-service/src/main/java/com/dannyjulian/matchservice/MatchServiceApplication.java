package com.dannyjulian.matchservice;

import com.dannyjulian.matchservice.model.MatchItem;
import com.dannyjulian.matchservice.repository.MatchRepository;
import com.dannyjulian.matchservice.util.BidOrAsk;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

@SpringBootApplication
public class MatchServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MatchServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner databaseDataInitialization(MatchRepository repository) {
		return args -> {
			MatchItem item1 = new MatchItem();
			item1.setQuantity(10);
			item1.setGuid("GF");
			item1.setPrice(BigDecimal.valueOf(5.1));
			item1.setBidOrAsk(BidOrAsk.BID);

			MatchItem item2 = new MatchItem();
			item2.setQuantity(1);
			item2.setGuid("GF");
			item2.setPrice(BigDecimal.valueOf(5.5));
			item2.setBidOrAsk(BidOrAsk.ASK);

			repository.save(item1);
			repository.save(item2);
		};

	}

}
