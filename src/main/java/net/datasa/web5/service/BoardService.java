package net.datasa.web5.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datasa.web5.domain.dto.BoardDTO;
import net.datasa.web5.domain.entity.BoardEntity;
import net.datasa.web5.repository.BoardRepository;
import net.datasa.web5.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
                .createDate(boardEntity.getCreateDate())
                .updateDate(boardEntity.getUpdateDate())
                .viewCount(boardEntity.getViewCount())
                .likeCount(boardEntity.getLikeCount())
                .fileName(boardEntity.getFileName())
                .build();
    }

    /**
     * 검색 결과 글목록을 지정한 한페이지 분량의 Page 객체로 리턴
     *
     * @param page       현재 페이지
     * @param pageSize   페이지당 글 수
     * @param searchType 검색 조건 (제목검색 : title, 본문검색:contents, 작성자검색:id)
     * @param searchWord 검색어
     * @return 게시글 목록 정보
     */
    public Page<BoardDTO> getList(int page, int pageSize, String searchType, String searchWord) {
        // 조회 조건을 담은 Pageable 객체 생성
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.Direction.DESC, "boardNum");
        // repository의 메소드로 Pageable 전달하여 조회, Page 리턴 받음
        return switch (searchType) {
            case "title" -> boardRepository.findByTitleContaining(searchWord, pageable)
                    .map(this::convertToDTO);
            case "contents" -> boardRepository.findByContentsContaining(searchWord, pageable)
                    .map(this::convertToDTO);
            case "id" -> boardRepository.findByMemberMemberIdContaining(searchWord, pageable)
                    .map(this::convertToDTO);
            default -> boardRepository.findAll(pageable)
                    .map(this::convertToDTO);
        };
    }

//    public List<BoardDTO> getList(int page, int pageSize, String searchType, String searchWord) {
//        // BoardRepository의 메소드를 호출하여 게시판의 모든 글 정보를 조회
//        // 엔티티의 개수만큼 반복하면서 엔티티의 값으 BoardDTO 객체를 생성하여 저장
//        Sort sort = Sort.by(Sort.DEFAULT_DIRECTION, "boardNum");
//        return boardRepository
//                .findByTitleContaining(searchWord, sort).stream().map(boardEntity ->
//                        BoardDTO.builder()
//                                .boardNum(boardEntity.getBoardNum())
//                                .memberId(boardEntity.getMember().getMemberId())
//                                .memberName(boardEntity.getMember().getMemberName())
//                                .title(boardEntity.getTitle())
//                                .contents(boardEntity.getContents())
//                                .createDate(boardEntity.getCreateDate())
//                                .updateDate(boardEntity.getUpdateDate())
//                                .viewCount(boardEntity.getViewCount())
//                                .likeCount(boardEntity.getLikeCount())
//                                .fileName(boardEntity.getFileName())
//                                .build())
//                .collect(Collectors.toList());
//    }

    private BoardDTO convertToDTO(BoardEntity boardEntity) {
        return BoardDTO.builder()
                .boardNum(boardEntity.getBoardNum())
                .memberId(boardEntity.getMember().getMemberId())
                .memberName(boardEntity.getMember().getMemberName())
                .title(boardEntity.getTitle())
                .contents(boardEntity.getContents())
                .createDate(boardEntity.getCreateDate())
                .updateDate(boardEntity.getUpdateDate())
                .viewCount(boardEntity.getViewCount())
                .likeCount(boardEntity.getLikeCount())
                .originalName(boardEntity.getOriginalName())
                .fileName(boardEntity.getFileName())
                .build();
    }

}
