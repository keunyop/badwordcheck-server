package com.badwordcheck.webservice.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
@Setter
@AllArgsConstructor
public class Alert {
    private long id;
    private String description;
    private boolean isDone;
    private Date targetDate;
}
