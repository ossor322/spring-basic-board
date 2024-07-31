package net.datasa.web5.domain.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Value;
import org.springframework.boot.autoconfigure.ssl.SslProperties;
import org.springframework.cglib.core.Local;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link net.datasa.web5.domain.entity.BoardEntity}
 */
@Value
@Builder
@Data
public class BoardDTO implements Serializable {
    Integer boardNum;
    String memberId;
    String memberName;
    String title;
    String contents;
    Integer viewCount;
    Integer likeCount;
    File file;
    String fileName;
    String originalName;
    LocalDateTime createDate;
    LocalDateTime updateDate;
}