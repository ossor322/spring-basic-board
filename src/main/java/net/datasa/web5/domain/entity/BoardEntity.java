package net.datasa.web5.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Slf4j
@Entity
@Table(name = "web5_board")
public class BoardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_num")
    private Integer boardNum;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", referencedColumnName = "member_id")
    private MemberEntity member;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "contents", nullable = false)
    private String contents;

    @Column(name = "view_count", columnDefinition = "default 0")
    private Integer viewCount;

    @Column(name = "like_count", columnDefinition = "default 0")
    private Integer likeCount;

    @Column(name = "original_name", length = 300)
    private String originalName;

    @Column(name = "file_name", length = 100)
    private String fileName;

    // 작성 시간
    @CreatedDate
    @Column(name = "create_date", columnDefinition = "default current_timestamp")
    private LocalDateTime createDate;

    // 수정 시간
    @LastModifiedDate
    @Column(name = "update_date", columnDefinition = "default current_timestamp on update current_timestamp")
    private LocalDateTime updateDate;
}
