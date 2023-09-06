package com.kh.youtube.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Subscribe {
    private int subsCode;
    private Date subsDate;

    private Member member;
    private Channel channel;
}
