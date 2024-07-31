package net.datasa.web5.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datasa.web5.domain.entity.MemberEntity;
import net.datasa.web5.repository.MemberRepository;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthenticatedUserDetailsService implements UserDetailsService {

	private final BCryptPasswordEncoder passwordEncoder;
	private final MemberRepository memberRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//전달받은 사용자 아이디 (username)으로 DB에서 사용자 정보 조회
		//아이디가 없으면 예외
		MemberEntity entity = memberRepository.findById(username)
				.orElseThrow(() -> new EntityNotFoundException("아이디가 없습니다."));
		
		//있으면 조회된 정보로 UserDetails객체 생성해서 리턴
		AuthenticatedUser user = AuthenticatedUser.builder()
				.id(username)
				.password(entity.getMemberPassword())
				.name(entity.getMemberName())
				.roleName(entity.getRolename())
				.enabled(entity.getEnabled())
				.build();
		
		log.debug("인증정보 : {}", user);
	
		return user;
	}

}
