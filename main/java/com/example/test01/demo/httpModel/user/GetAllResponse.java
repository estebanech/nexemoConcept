package com.example.test01.demo.httpModel.user;

import com.example.test01.demo.entity.UserIn;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetAllResponse {
    private List<UserIn> users;
}
