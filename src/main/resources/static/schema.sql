create table web5_member
(
    member_id       varchar(30) primary key,                                                        -- 회원을 구분하는 아이디
    member_password varchar(100) not null,                                                          -- 비밀번호(암호화)
    member_name     varchar(30)  not null,                                                          -- 회원 이름
    email           varchar(50),                                                                    -- 이메일
    phone           varchar(30),                                                                    -- 전화번호
    address         varchar(200),                                                                   -- 주소
    enabled         tinyint(1)  default 1 check (enabled in (0, 1)),                                -- 계정상태. 1:사용가능, 0:사용불가능
    rolename        varchar(30) default 'role_user' check (rolename in ('ROLE_USER', 'ROLE_ADMIN')) -- 사용자 구분. 'role_user', 'role_admin' 중 하나
);

select *
from web5_member;

create table web5_board
(
    board_num     int auto_increment primary key,      -- 게시글 일련번호
    member_id     varchar(30),                         -- 작성자 id (외래 키)
    title         varchar(1000) not null,              -- 글제목
    contents      text          not null,              -- 글내용
    view_count    int       default 0,                 -- 조회
    like_count    int       default 0,                 -- 추천
    original_name varchar(300),                        -- 첨부파일 원래 이
    file_name     varchar(100),                        -- 첨부파일 저장된 이름
    create_date   timestamp default current_timestamp, -- 작성 시간
    update_date   timestamp default current_timestamp
        on update current_timestamp,                   -- 수정 시간
    constraint foreign key (member_id)
        references web5_member (member_id) on delete set null
);

insert into web5_board (member_id, title, contents)
values ('abc', '제목이지롱~3', '내용이지롱~~~3');

select * from web5_board;