
-- code_group
insert into code_group values ('GENDER','성별코드','성별을표시');
insert into code_group values ('VISIT','방문상태코드','환자 방문의 상태(방문중, 종료, 취소)');
insert into code_group values ('TREAT_SUBJECT','진료과목코드','진료과목(내과, 안과 등)');
insert into code_group values ('TREAT_TYPE','진료유형코드','진료의 유형(약처방, 검사 등)');

-- code
insert into code values ('M','GENDER','남');
insert into code values ('F','GENDER','여');
insert into code values ('1','VISIT','방문중');
insert into code values ('2','VISIT','종료');
insert into code values ('3','VISIT','취소');
insert into code values ('01','TREAT_SUBJECT','내과');
insert into code values ('02','TREAT_SUBJECT','안과');
insert into code values ('D','TREAT_TYPE','약처방');
insert into code values ('T','TREAT_TYPE','검사');

