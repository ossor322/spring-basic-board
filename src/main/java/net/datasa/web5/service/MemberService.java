package net.datasa.web5.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datasa.web5.domain.dto.MemberDTO;
import net.datasa.web5.domain.entity.MemberEntity;
import net.datasa.web5.repository.MemberRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 회원정보 관련 처리 서비스
 */
@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class MemberService {
    //회원정보 DB 작업
    private final MemberRepository memberRepository;
    //암호화
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * 가입처리
     *
     * @param dto 가입 데이터
     */
    public void join(MemberDTO dto) {

        //dto의 값을 읽어서 엔티티 생성
        MemberEntity entity = MemberEntity.builder()
                .memberId(dto.getMemberId())
                .memberPassword(passwordEncoder.encode(dto.getMemberPassword()))
                .memberName(dto.getMemberName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .enabled(true)
                .rolename("ROLE_USER")
                .build();
        //엔티티를 저장
        memberRepository.save(entity);
    }

    //ID를 조회하여 신규 가입자가 써도 되는 아이디인지 확인하는 메소드
    public boolean isAvailableId(String searchId) {
        //전달받은 아이디를 데이터베이스에서 조회
        //결과가 없으면 true 를 리턴
        //결과가 있으면 false 를 리턴
        return memberRepository.findById(searchId).isEmpty();
    }

    public MemberDTO getMember(String memberName) {
        MemberEntity foundMemberEntity = memberRepository.findById(memberName)
                .orElseThrow(() -> new EntityNotFoundException("memberId : {} Entity not found"));

        return MemberDTO.builder()
                .memberId(foundMemberEntity.getMemberId())
                .memberName(foundMemberEntity.getMemberName())
                .email(foundMemberEntity.getEmail())
                .phone(foundMemberEntity.getPhone())
                .address(foundMemberEntity.getAddress())
                .build();
    }

    /**
     * 개인정보 수정
     * @param memberDTO 수정할 정보
     */
    public void update(MemberDTO memberDTO) {
        MemberEntity entity = memberRepository.findById(memberDTO.getMemberId())
                .orElseThrow(() -> new EntityNotFoundException("memberId : {} Entity not found"));

        entity.setMemberName(memberDTO.getMemberName());
        entity.setEmail(memberDTO.getEmail());
        entity.setPhone(memberDTO.getPhone());
        entity.setAddress(memberDTO.getAddress());

        // 비밀번호는 전달된 값이 있을때에만 암호화해서 수정
        if (!(memberDTO.getMemberPassword() == null || memberDTO.getMemberPassword().isBlank()))
            entity.setMemberPassword(passwordEncoder.encode(memberDTO.getMemberPassword()));

        // 리턴될때 DB에 저장됨
    }
}