package com.onemoney.recgardecardservice.client;

import com.onemoney.recgardecardservice.service.dto.AccountDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "account-service")
public interface AccountClient {
    @GetMapping("api/accounts/user/{userId}")
    AccountDTO getAccountByUserId(@PathVariable("userId") String userId);
}
