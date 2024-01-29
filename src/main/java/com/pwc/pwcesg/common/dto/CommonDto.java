package com.pwc.pwcesg.common.dto;

import javax.ws.rs.DefaultValue;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CommonDto {

    private int totalCount; // 폐이징 : 데이터 전체 ROW 건수

    @DefaultValue("1")
    private int pageNumber; // 페이징 : 현재 Page 번호

    private int totalPage; // 페이징 : 전체 Page 건수

    @DefaultValue("10")
    private int pageSize; // 페이징 : Page 크기 건수

    private int startRow; // 페이징 : ROW 시작 번호

    private int endRow; // 페이징 : ROW 종료 번호

    private String paging; // 페이징 : 폐이징 여부
}