package net.datasa.web5.repository;

import net.datasa.web5.domain.entity.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Integer> {
    List<BoardEntity> findByTitleContaining(String title, Sort sort);

    Page<BoardEntity> findByTitleContaining(String title, Pageable pageable);

    Page<BoardEntity> findByContentsContaining(String title, Pageable pageable);

    Page<BoardEntity> findByMemberMemberIdContaining(String memberId, Pageable pageable);
}
