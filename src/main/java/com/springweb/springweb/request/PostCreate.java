package com.springweb.springweb.request;


import com.springweb.springweb.exception.InvalidRequest;
import lombok.*;

import javax.validation.constraints.NotBlank;

@ToString
@Setter@Getter
public class PostCreate {


    @NotBlank(message = "타이틀을 입력해 주세요")
    public String title;

    @NotBlank(message = "컨턴트를 입력해 주세요")
    public String content;

    @Builder
    public PostCreate(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void validate() {
        if(title.contains("바보")){
            throw new InvalidRequest();
        }
    }
}
