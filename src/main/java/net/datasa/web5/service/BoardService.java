package net.datasa.web5.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datasa.web5.domain.dto.BoardDTO;
import net.datasa.web5.domain.entity.BoardEntity;
import net.datasa.web5.repository.BoardRepository;
import net.datasa.web5.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Slf4j
@Service
public class BoardService {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    /**
     * @param username
     * @param boardDTO
     * @return 저장한 boardNum
     */
    public Integer write(String username, BoardDTO boardDTO) {
        BoardEntity boardEntity = new BoardEntity();
        memberRepository.findById(username).ifPresent(boardEntity::setMember);
        boardEntity.setTitle(boardDTO.getTitle());
        boardEntity.setContents(boardDTO.getContents());
        // file 처리하기
        log.debug("board : {}", boardEntity);
        return boardRepository.save(boardEntity).getBoardNum();
    }

    public BoardDTO findByBoardNum(Integer boardNum) {
        BoardEntity boardEntity = boardRepository.findById(boardNum).orElseThrow(
                () -> new EntityNotFoundException("boardNum : " + boardNum + " 존재하지 않습니다.")
        );

        return BoardDTO.builder()
                .boardNum(boardEntity.getBoardNum())
                .memberId(boardEntity.getMember().getMemberId())
                .memberName(boardEntity.getMember().getMemberName())
                .title(boardEntity.getTitle())
                .contents(boardEntity.getContents())
                .viewCount(boardEntity.getViewCount())
                .likeCount(boardEntity.getLikeCount())
                .fileName(boardEntity.getFileName())
                .build();
    }

    public List<BoardDTO> getList() {
        // BoardRepository의 메소드를 호출하여 게시판의 모든 글 정보를 조회
        // 엔티티의 개수만큼 반복하면서 엔티티의 값으 BoardDTO 객체를 생성하여 저장
        Sort sort 
        return boardRepository
                .findAll().stream().map(boardEntity ->
                        BoardDTO.builder()
                                .boardNum(boardEntity.getBoardNum())
                                .memberId(boardEntity.getMember().getMemberId())
                                .memberName(boardEntity.getMember().getMemberName())
                                .title(boardEntity.getTitle())
                                .contents(boardEntity.getContents())
                                .viewCount(boardEntity.getViewCount())
                                .likeCount(boardEntity.getLikeCount())
                                .fileName(boardEntity.getFileName())
                                .build())
                .collect(Collectors.toList());
    }
}
