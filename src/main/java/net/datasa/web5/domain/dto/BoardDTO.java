package net.datasa.web5.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@Data
public class BoardDTO{
    private Integer boardNum;
    private String memberId;
    private String memberName;
    private String title;
    private String contents;
    private Integer viewCount;
    private Integer likeCount;
    private File file;
    private String fileName;
    private String originalName;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
}