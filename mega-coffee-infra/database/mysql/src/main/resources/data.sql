-- 매장
INSERT INTO store (id, created_at, updated_at, city, street, zip_code, name, owner_id, closing_time, opening_time, type)
VALUES
    (1, CURRENT_TIMESTAMP(6), NULL, '서울', '강남구 강남대로 123번길', '06132', '메가커피 강남점', 1, '22:00:00', '07:00:00', 'FRANCHISE'),
    (2, CURRENT_TIMESTAMP(6), NULL, '서울', '마포구 홍익로 456번길', '04056', '메가커피 홍대점', 2, '22:30:00', '08:00:00', 'COMPANY_OWNED'),
    (3, CURRENT_TIMESTAMP(6), NULL, '부산', '해운대구 해운대로 789번길', '48100', '메가커피 해운대점', 25, '21:00:00', '06:30:00', 'FRANCHISE'),
    (4, CURRENT_TIMESTAMP(6), NULL, '부산', '사하구 사하로 1010번길', '49494', '메가커피 사하점', NULL, '21:30:00', '07:30:00', 'COMPANY_OWNED'),
    (5, CURRENT_TIMESTAMP(6), NULL, '인천', '남동구 논현로 1111번길', '21563', '메가커피 인천점', NULL, '22:00:00', '07:00:00', 'FRANCHISE'),
    (6, CURRENT_TIMESTAMP(6), NULL, '인천', '연수구 아트센터대로 1212번길', '22332', '메가커피 연수점', 29, '21:30:00', '06:30:00', 'COMPANY_OWNED'),
    (7, CURRENT_TIMESTAMP(6), NULL, '대구', '달서구 대구로 1313번길', '42345', '메가커피 대구점', NULL, '22:30:00', '07:30:00', 'FRANCHISE'),
    (8, CURRENT_TIMESTAMP(6), NULL, '대구', '수성구 청수로 1414번길', '45677', '메가커피 수성점', 33, '21:00:00', '06:00:00', 'COMPANY_OWNED'),
    (9, CURRENT_TIMESTAMP(6), NULL, '경기', '수원시 팔달구 팔달로 1515번길', '16441', '메가커피 수원점', 9, '22:00:00', '07:00:00', 'FRANCHISE'),
    (10, CURRENT_TIMESTAMP(6), NULL, '경기', '용인시 기흥구 동백로 1616번길', '17171', '메가커피 용인점', NULL, '21:30:00', '06:30:00', 'COMPANY_OWNED'),
    (11, CURRENT_TIMESTAMP(6), NULL, '경기', '성남시 분당구 판교로 1717번길', '13489', '메가커피 분당점', 37, '22:00:00', '07:00:00', 'FRANCHISE'),
    (12, CURRENT_TIMESTAMP(6), NULL, '경기', '고양시 덕양구 화신로 1818번길', '10521', '메가커피 고양점', 41, '21:30:00', '06:30:00', 'COMPANY_OWNED'),
    (13, CURRENT_TIMESTAMP(6), NULL, '서울', '강서구 방화대로 1919번길', '07595', '메가커피 강서점', 45, '22:30:00', '07:30:00', 'FRANCHISE'),
    (14, CURRENT_TIMESTAMP(6), NULL, '서울', '노원구 노원로 2020번길', '01897', '메가커피 노원점', 49, '21:00:00', '06:00:00', 'COMPANY_OWNED'),
    (15, CURRENT_TIMESTAMP(6), NULL, '부산', '서구 대신로 2121번길', '49100', '메가커피 대신점', NULL, '22:00:00', '07:00:00', 'FRANCHISE'),
    (16, CURRENT_TIMESTAMP(6), NULL, '부산', '동구 중앙대로 2222번길', '48991', '메가커피 동구점', NULL, '21:30:00', '06:30:00', 'COMPANY_OWNED'),
    (17, CURRENT_TIMESTAMP(6), NULL, '인천', '부평구 청천로 2323번길', '21300', '메가커피 부평점', NULL, '22:30:00', '07:30:00', 'FRANCHISE'),
    (18, CURRENT_TIMESTAMP(6), NULL, '인천', '계양구 경명로 2424번길', '21136', '메가커피 계양점', 12, '21:00:00', '06:00:00', 'COMPANY_OWNED'),
    (19, CURRENT_TIMESTAMP(6), NULL, '대구', '북구 칠곡중앙대로 2525번길', '41554', '메가커피 칠곡점', NULL, '22:00:00', '07:00:00', 'FRANCHISE'),
    (20, CURRENT_TIMESTAMP(6), NULL, '대구', '달성군 가창면 논공로 2626번길', '42922', '메가커피 가창점', NULL, '21:30:00', '06:30:00', 'COMPANY_OWNED'),
    (21, CURRENT_TIMESTAMP(6), NULL, '경기', '성남시 분당구 야탑로 2727번길', '13590', '메가커피 야탑점', NULL, '22:30:00', '07:30:00', 'FRANCHISE'),
    (22, CURRENT_TIMESTAMP(6), NULL, '경기', '고양시 일산동구 중앙로 2828번길', '10452', '메가커피 일산점', NULL, '21:00:00', '06:00:00', 'COMPANY_OWNED'),
    (23, CURRENT_TIMESTAMP(6), NULL, '서울', '양천구 목동중앙북로 2929번길', '08081', '메가커피 목동점', NULL, '22:00:00', '07:00:00', 'FRANCHISE'),
    (24, CURRENT_TIMESTAMP(6), NULL, '서울', '은평구 연서로 3030번길', '03322', '메가커피 은평점', 18, '21:30:00', '06:30:00', 'COMPANY_OWNED'),
    (25, CURRENT_TIMESTAMP(6), NULL, '부산', '금정구 금강로 3131번길', '46259', '메가커피 금정점', NULL, '22:30:00', '07:30:00', 'FRANCHISE'),
    (26, CURRENT_TIMESTAMP(6), NULL, '부산', '동래구 사직로 3232번길', '47891', '메가커피 동래점', NULL, '21:00:00', '06:00:00', 'COMPANY_OWNED'),
    (27, CURRENT_TIMESTAMP(6), NULL, '인천', '남동구 성리로 3333번길', '21536', '메가커피 성리점', 21, '22:00:00', '07:00:00', 'FRANCHISE'),
    (28, CURRENT_TIMESTAMP(6), NULL, '인천', '연수구 송도과학로 3434번길', '22004', '메가커피 송도점', NULL, '21:30:00', '06:30:00', 'COMPANY_OWNED'),
    (29, CURRENT_TIMESTAMP(6), NULL, '대구', '수성구 동대구로 3535번길', '42175', '메가커피 동대구점', NULL, '22:30:00', '07:30:00', 'FRANCHISE'),
    (30, CURRENT_TIMESTAMP(6), NULL, '대구', '달서구 상화북로 3636번길', '42702', '메가커피 상화점', NULL, '21:00:00', '06:00:00', 'COMPANY_OWNED');

-- 유저
INSERT INTO user (id, created_at, updated_at, city, street, zip_code, is_deleted, email, employee_type, name, phone, store_id, work_time_type)
VALUES
    (1, CURRENT_TIMESTAMP(6), NULL, '서울', '강남구 역삼로 123번길', '06132', 0, 'owner1@example.com', 'OWNER', 'Owner 1', '010-1234-5678', 1, 'WEEKDAY'),
    (2, CURRENT_TIMESTAMP(6), NULL, '서울', '마포구 망원로 456번길', '04056', 0, 'owner2@example.com', 'OWNER', 'Owner 2', '010-9876-5432', 2, 'WEEKDAY'),
    (3, CURRENT_TIMESTAMP(6), NULL, '서울', '송파구 백제고분로 789번길', '05578', 0, 'super_manager1@example.com', 'SUPER_MANAGER', 'Super Manager 1', '010-1111-2222', 3, 'WEEKEND'),
    (4, CURRENT_TIMESTAMP(6), NULL, '서울', '서초구 강남대로 1010번길', '06510', 0, 'super_manager2@example.com', 'SUPER_MANAGER', 'Super Manager 2', '010-3333-4444', 4, 'WEEKEND'),
    (5, CURRENT_TIMESTAMP(6), NULL, '부산', '해운대구 해운대로 111번길', '48100', 0, 'manager1@example.com', 'MANAGER', 'Manager 1', '010-5555-6666', 5, 'WEEKDAY'),
    (6, CURRENT_TIMESTAMP(6), NULL, '부산', '사하구 하단로 222번길', '49494', 0, 'manager2@example.com', 'MANAGER', 'Manager 2', '010-7777-8888', 6, 'WEEKDAY'),
    (7, CURRENT_TIMESTAMP(6), NULL, '인천', '남동구 논현로 333번길', '21563', 0, 'part_time1@example.com', 'PART_TIME', 'Part Time 1', '010-9999-0000', 7, 'WEEKEND'),
    (8, CURRENT_TIMESTAMP(6), NULL, '인천', '연수구 아트센터대로 444번길', '22332', 0, 'part_time2@example.com', 'PART_TIME', 'Part Time 2', '010-0000-1111', 7, 'WEEKEND'),
    (9, CURRENT_TIMESTAMP(6), NULL, '대구', '달서구 월배로 555번길', '42345', 0, 'owner3@example.com', 'OWNER', 'Owner 3', '010-7777-7777', 9, 'WEEKDAY'),
    (10, CURRENT_TIMESTAMP(6), NULL, '대구', '수성구 청수로 666번길', '45677', 0, 'super_manager3@example.com', 'SUPER_MANAGER', 'Super Manager 3', '010-9999-9999', 10, 'WEEKDAY'),
    (11, CURRENT_TIMESTAMP(6), NULL, '울산', '남구 성남로 777번길', '44766', 0, 'part_time5@example.com', 'PART_TIME', 'Part Time 5', '010-5555-5555', 7, 'WEEKEND'),
    (12, CURRENT_TIMESTAMP(6), NULL, '대전', '서구 둔산로 888번길', '35242', 0, 'owner6@example.com', 'OWNER', 'Owner 6', '010-6666-6666', 18, 'WEEKDAY'),
    (13, CURRENT_TIMESTAMP(6), NULL, '대전', '유성구 봉명로 999번길', '34101', 0, 'super_manager4@example.com', 'SUPER_MANAGER', 'Super Manager 4', '010-8888-8888', 19, 'WEEKEND'),
    (14, CURRENT_TIMESTAMP(6), NULL, '경기', '수원시 팔달구 팔달로 1010번길', '16441', 0, 'manager3@example.com', 'MANAGER', 'Manager 3', '010-2222-2222', 7, 'WEEKDAY'),
    (15, CURRENT_TIMESTAMP(6), NULL, '경기', '용인시 처인구 백령로 1212번길', '16970', 0, 'manager4@example.com', 'MANAGER', 'Manager 4', '010-3333-3333', 7, 'WEEKDAY'),
    (16, CURRENT_TIMESTAMP(6), NULL, '경기', '성남시 분당구 황새울로 1313번길', '13456', 0, 'part_time6@example.com', 'PART_TIME', 'Part Time 6', '010-6666-7777', 7, 'WEEKEND'),
    (17, CURRENT_TIMESTAMP(6), NULL, '강원', '춘천시 남산면 춘천로 1414번길', '20000', 0, 'manager5@example.com', 'MANAGER', 'Manager 5', '010-7777-8888', 23, 'WEEKDAY'),
    (18, CURRENT_TIMESTAMP(6), NULL, '강원', '원주시 호저면 호저로 1515번길', '26400', 0, 'owner7@example.com', 'OWNER', 'Owner 7', '010-9999-1111', 24, 'WEEKDAY'),
    (19, CURRENT_TIMESTAMP(6), NULL, '충북', '청주시 흥덕구 덕암로 1616번길', '28602', 0, 'super_manager5@example.com', 'SUPER_MANAGER', 'Super Manager 5', '010-2222-3333', 25, 'WEEKEND'),
    (20, CURRENT_TIMESTAMP(6), NULL, '충북', '제천시 청풍면 제천로 1717번길', '27300', 0, 'part_time7@example.com', 'PART_TIME', 'Part Time 7', '010-4444-5555', 26, 'WEEKEND'),
    (21, CURRENT_TIMESTAMP(6), NULL, '경남', '창원시 의창구 창원로 1818번길', '51400', 0, 'owner8@example.com', 'OWNER', 'Owner 8', '010-8888-9999', 27, 'WEEKDAY'),
    (22, CURRENT_TIMESTAMP(6), NULL, '경남', '진주시 상봉로 1919번길', '52900', 0, 'super_manager6@example.com', 'SUPER_MANAGER', 'Super Manager 6', '010-1111-4444', 28, 'WEEKDAY'),
    (23, CURRENT_TIMESTAMP(6), NULL, '전남', '여수시 봉양로 2020번길', '59600', 0, 'manager6@example.com', 'MANAGER', 'Manager 6', '010-2222-5555', 29, 'WEEKEND'),
    (24, CURRENT_TIMESTAMP(6), NULL, '전남', '순천시 성지로 2121번길', '58000', 0, 'part_time8@example.com', 'PART_TIME', 'Part Time 8', '010-3333-6666', 30, 'WEEKEND'),
    (25, CURRENT_TIMESTAMP(6), NULL, '전북', '전주시 완산구 홍산로 2222번길', '55100', 0, 'owner9@example.com', 'OWNER', 'Owner 9', '010-7777-0000', 3, 'WEEKDAY'),
    (26, CURRENT_TIMESTAMP(6), NULL, '전북', '익산시 인화로 2323번길', '54500', 0, 'super_manager7@example.com', 'SUPER_MANAGER', 'Super Manager 7', '010-9999-2222', 11, 'WEEKDAY'),
    (27, CURRENT_TIMESTAMP(6), NULL, '충남', '천안시 동남구 충무로 2424번길', '31100', 0, 'manager7@example.com', 'MANAGER', 'Manager 7', '010-4444-7777', 12, 'WEEKEND'),
    (28, CURRENT_TIMESTAMP(6), NULL, '충남', '아산시 배방읍 무봉로 2525번길', '31500', 0, 'part_time9@example.com', 'PART_TIME', 'Part Time 9', '010-5555-9999', 13, 'WEEKEND'),
    (29, CURRENT_TIMESTAMP(6), NULL, '강원', '원주시 원문로 2626번길', '26401', 0, 'owner10@example.com', 'OWNER', 'Owner 10', '010-6666-1111', 6, 'WEEKDAY'),
    (30, CURRENT_TIMESTAMP(6), NULL, '강원', '춘천시 남산면 춘천로 2727번길', '20000', 0, 'super_manager8@example.com', 'SUPER_MANAGER', 'Super Manager 8', '010-8888-3333', 14, 'WEEKDAY'),
    (31, CURRENT_TIMESTAMP(6), NULL, '경기', '성남시 분당구 불정로 2828번길', '13530', 0, 'manager8@example.com', 'MANAGER', 'Manager 8', '010-1111-7777', 15, 'WEEKDAY'),
    (32, CURRENT_TIMESTAMP(6), NULL, '경기', '용인시 기흥구 구성로 2929번길', '17171', 0, 'part_time10@example.com', 'PART_TIME', 'Part Time 10', '010-2222-8888', 16, 'WEEKDAY'),
    (33, CURRENT_TIMESTAMP(6), NULL, '서울', '마포구 양화로 3030번길', '04031', 0, 'owner11@example.com', 'OWNER', 'Owner 11', '010-3333-1111', 8, 'WEEKEND'),
    (34, CURRENT_TIMESTAMP(6), NULL, '서울', '강서구 공항대로 3131번길', '07505', 0, 'super_manager9@example.com', 'SUPER_MANAGER', 'Super Manager 9', '010-4444-2222', 17, 'WEEKEND'),
    (35, CURRENT_TIMESTAMP(6), NULL, '부산', '해운대구 해운대로 3232번길', '48100', 0, 'manager9@example.com', 'MANAGER', 'Manager 9', '010-5555-3333', 18, 'WEEKDAY'),
    (36, CURRENT_TIMESTAMP(6), NULL, '부산', '사하구 하단로 3333번길', '49494', 0, 'part_time11@example.com', 'PART_TIME', 'Part Time 11', '010-6666-4444', 19, 'WEEKDAY'),
    (37, CURRENT_TIMESTAMP(6), NULL, '인천', '남동구 논현로 3434번길', '21563', 0, 'owner12@example.com', 'OWNER', 'Owner 12', '010-7777-5555', 11, 'WEEKEND'),
    (38, CURRENT_TIMESTAMP(6), NULL, '인천', '연수구 아트센터대로 3535번길', '22332', 0, 'super_manager10@example.com', 'SUPER_MANAGER', 'Super Manager 10', '010-8888-6666', 20, 'WEEKEND'),
    (39, CURRENT_TIMESTAMP(6), NULL, '대구', '달서구 월배로 3636번길', '42345', 0, 'manager10@example.com', 'MANAGER', 'Manager 10', '010-9999-7777', 21, 'WEEKDAY'),
    (40, CURRENT_TIMESTAMP(6), NULL, '대구', '수성구 청수로 3737번길', '45677', 0, 'part_time12@example.com', 'PART_TIME', 'Part Time 12', '010-0000-8888', 22, 'WEEKDAY'),
    (41, CURRENT_TIMESTAMP(6), NULL, '울산', '남구 성남로 3838번길', '44766', 0, 'owner13@example.com', 'OWNER', 'Owner 13', '010-1111-8888', 12, 'WEEKDAY'),
    (42, CURRENT_TIMESTAMP(6), NULL, '대전', '서구 둔산로 3939번길', '35242', 0, 'super_manager11@example.com', 'SUPER_MANAGER', 'Super Manager 11', '010-2222-9999', 23, 'WEEKDAY'),
    (43, CURRENT_TIMESTAMP(6), NULL, '대전', '유성구 봉명로 4040번길', '34101', 0, 'manager11@example.com', 'MANAGER', 'Manager 11', '010-3333-0000', 24, 'WEEKEND'),
    (44, CURRENT_TIMESTAMP(6), NULL, '경기', '수원시 팔달구 팔달로 4141번길', '16441', 0, 'part_time13@example.com', 'PART_TIME', 'Part Time 13', '010-4444-1111', 25, 'WEEKEND'),
    (45, CURRENT_TIMESTAMP(6), NULL, '경기', '용인시 처인구 백령로 4242번길', '16970', 0, 'owner14@example.com', 'OWNER', 'Owner 14', '010-5555-2222', 13, 'WEEKDAY'),
    (46, CURRENT_TIMESTAMP(6), NULL, '경기', '성남시 수정구 성남로 4343번길', '13100', 0, 'super_manager12@example.com', 'SUPER_MANAGER', 'Super Manager 12', '010-6666-3333', 26, 'WEEKDAY'),
    (47, CURRENT_TIMESTAMP(6), NULL, '서울', '강남구 논현로 4444번길', '06132', 0, 'manager12@example.com', 'MANAGER', 'Manager 12', '010-7777-4444', 27, 'WEEKEND'),
    (48, CURRENT_TIMESTAMP(6), NULL, '서울', '송파구 올림픽로 4545번길', '05578', 0, 'part_time14@example.com', 'PART_TIME', 'Part Time 14', '010-8888-5555', 28, 'WEEKEND'),
    (49, CURRENT_TIMESTAMP(6), NULL, '부산', '해운대구 해운대로 4646번길', '48100', 0, 'owner15@example.com', 'OWNER', 'Owner 15', '010-9999-6666', 14, 'WEEKDAY'),
    (50, CURRENT_TIMESTAMP(6), NULL, '부산', '사하구 하단로 4747번길', '49494', 0, 'super_manager13@example.com', 'SUPER_MANAGER', 'Super Manager 13', '010-0000-7777', 29, 'WEEKDAY');

-- 교육 프로그램 데이터
INSERT INTO education (id, created_at, updated_at, content, name)
VALUES
    (1, CURRENT_TIMESTAMP(6), NULL, '이론과 실습을 통한 커피 추출과 조리 방법에 대해 학습하는 교육 프로그램입니다.', '커피 추출과 조리 기초 교육'),
    (2, CURRENT_TIMESTAMP(6), NULL, '다양한 커피 음료 및 레시피에 대해 학습하는 교육 프로그램입니다.', '커피 음료와 레시피 교육'),
    (3, CURRENT_TIMESTAMP(6), NULL, '커피 원두의 특성과 맛을 평가하는 교육 프로그램입니다.', '커피 맛 평가 교육'),
    (4, CURRENT_TIMESTAMP(6), NULL, '고객 상담 및 서비스에 대해 학습하는 교육 프로그램입니다.', '고객 상담 및 서비스 교육'),
    (5, CURRENT_TIMESTAMP(6), NULL, '커피 원두의 품질과 저장 방법에 대해 학습하는 교육 프로그램입니다.', '커피 원두 관리 교육'),
    (6, CURRENT_TIMESTAMP(6), NULL, '디저트와 커피 매칭에 대해 학습하는 교육 프로그램입니다.', '디저트와 커피 매칭 교육'),
    (7, CURRENT_TIMESTAMP(6), NULL, '스페셜티 커피와 라떼아트에 대해 학습하는 교육 프로그램입니다.', '스페셜티 커피와 라떼아트 교육'),
    (8, CURRENT_TIMESTAMP(6), NULL, '커피 블렌딩과 로스팅에 대해 학습하는 교육 프로그램입니다.', '커피 블렌딩과 로스팅 교육'),
    (9, CURRENT_TIMESTAMP(6), NULL, '다양한 커피 장비와 기기 사용에 대해 학습하는 교육 프로그램입니다.', '커피 장비 사용 교육'),
    (10, CURRENT_TIMESTAMP(6), NULL, '커피 전문점 운영과 매장 관리에 대해 학습하는 교육 프로그램입니다.', '매장 운영과 관리 교육');

-- 교육별 수강 대상자 데이터
INSERT INTO education_employee_type (education_id, employee_type)
VALUES
    (1, 'PART_TIME'),
    (2, 'MANAGER'),
    (2, 'PART_TIME'),
    (3, 'MANAGER'),
    (3, 'PART_TIME'),
    (4, 'MANAGER'),
    (4, 'PART_TIME'),
    (5, 'SUPER_MANAGER'),
    (5, 'MANAGER'),
    (5, 'PART_TIME'),
    (6, 'MANAGER'),
    (6, 'PART_TIME'),
    (7, 'MANAGER'),
    (7, 'PART_TIME'),
    (8, 'MANAGER'),
    (8, 'PART_TIME'),
    (9, 'SUPER_MANAGER'),
    (9, 'MANAGER'),
    (9, 'PART_TIME'),
    (10, 'OWNER'),
    (10, 'SUPER_MANAGER');

-- 교육프로그램별 교육 장소
-- 교육 프로그램 1 - 커피 추출과 조리 기초 교육
INSERT INTO education_place (id, created_at, updated_at, city, street, zip_code, date, current_participant, max_participant, end_time, start_time, education_id)
VALUES
    (1, CURRENT_TIMESTAMP(6), NULL, '서울', '강남구 테헤란로 123번길', '06132', '2023-12-15', 0, 2, '17:00:00', '14:00:00', 1),
    (2, CURRENT_TIMESTAMP(6), NULL, '서울', '서초구 반포대로 456번길', '06577', '2023-12-16', 0, 3, '17:00:00', '14:00:00', 1),
    (3, CURRENT_TIMESTAMP(6), NULL, '인천', '남동구 성리로 789번길', '21536', '2023-12-17', 0, 5, '17:00:00', '14:00:00', 1);

-- 교육 프로그램 2 - 커피 음료와 레시피 교육
INSERT INTO education_place (id, created_at, updated_at, city, street, zip_code, date, current_participant, max_participant, end_time, start_time, education_id)
VALUES
    (4, CURRENT_TIMESTAMP(6), NULL, '부산', '해운대구 해운대로 111번길', '48100', '2023-12-15', 0, 3, '18:00:00', '15:00:00', 2),
    (5, CURRENT_TIMESTAMP(6), NULL, '부산', '사하구 하단로 222번길', '49494', '2023-12-16', 0, 5, '18:00:00', '15:00:00', 2),
    (6, CURRENT_TIMESTAMP(6), NULL, '대구', '수성구 중동로 333번길', '42185', '2023-12-17', 0, 7, '18:00:00', '15:00:00', 2);

-- 교육 프로그램 3 - 커피 맛 평가 교육
INSERT INTO education_place (id, created_at, updated_at, city, street, zip_code, date, current_participant, max_participant, end_time, start_time, education_id)
VALUES
    (7, CURRENT_TIMESTAMP(6), NULL, '경기', '성남시 분당구 성남대로 444번길', '13489', '2023-12-15', 0, 2, '16:00:00', '13:00:00', 3),
    (8, CURRENT_TIMESTAMP(6), NULL, '경기', '고양시 일산동구 일산로 555번길', '10452', '2023-12-16', 0, 5, '16:00:00', '13:00:00', 3),
    (9, CURRENT_TIMESTAMP(6), NULL, '서울', '양천구 목동중앙북로 666번길', '08081', '2023-12-17', 0, 3, '16:00:00', '13:00:00', 3);

-- 교육 프로그램 4 - 고객 상담 및 서비스 교육
INSERT INTO education_place (id, created_at, updated_at, city, street, zip_code, date, current_participant, max_participant, end_time, start_time, education_id)
VALUES
    (10, CURRENT_TIMESTAMP(6), NULL, '서울', '강동구 천호대로 777번길', '05364', '2023-12-15', 0, 30, '17:00:00', '14:00:00', 4),
    (11, CURRENT_TIMESTAMP(6), NULL, '서울', '관악구 관악로 888번길', '08776', '2023-12-16', 0, 15, '17:00:00', '14:00:00', 4),
    (12, CURRENT_TIMESTAMP(6), NULL, '경기', '성남시 수정구 수정로 999번길', '13485', '2023-12-17', 0, 20, '17:00:00', '14:00:00', 4);

-- 교육 프로그램 5 - 커피 원두 관리 교육
INSERT INTO education_place (id, created_at, updated_at, city, street, zip_code, date, current_participant, max_participant, end_time, start_time, education_id)
VALUES
    (13, CURRENT_TIMESTAMP(6), NULL, '인천', '남동구 인주대로 1010번길', '21546', '2023-12-15', 0, 5, '18:00:00', '15:00:00', 5),
    (14, CURRENT_TIMESTAMP(6), NULL, '인천', '연수구 연수로 1111번길', '22004', '2023-12-16', 0, 15, '18:00:00', '15:00:00', 5),
    (15, CURRENT_TIMESTAMP(6), NULL, '대구', '수성구 고산로 1212번길', '42178', '2023-12-17', 0, 25, '18:00:00', '15:00:00', 5);

-- 교육 프로그램 6 - 디저트와 커피 매칭 교육
INSERT INTO education_place (id, created_at, updated_at, city, street, zip_code, date, current_participant, max_participant, end_time, start_time, education_id)
VALUES
    (16, CURRENT_TIMESTAMP(6), NULL, '경기', '성남시 중원구 중앙로 1313번길', '13585', '2023-12-15', 0, 10, '16:00:00', '13:00:00', 6),
    (17, CURRENT_TIMESTAMP(6), NULL, '경기', '고양시 덕양구 고양대로 1414번길', '10522', '2023-12-16', 0, 20, '16:00:00', '13:00:00', 6),
    (18, CURRENT_TIMESTAMP(6), NULL, '서울', '영등포구 여의나루로 1515번길', '07311', '2023-12-17', 0, 4, '16:00:00', '13:00:00', 6);

-- 교육 프로그램 7 - 스페셜티 커피와 라떼아트 교육
INSERT INTO education_place (id, created_at, updated_at, city, street, zip_code, date, current_participant, max_participant, end_time, start_time, education_id)
VALUES
    (19, CURRENT_TIMESTAMP(6), NULL, '부산', '남구 암남로 1616번길', '48563', '2023-12-15', 0, 5, '19:00:00', '16:00:00', 7),
    (20, CURRENT_TIMESTAMP(6), NULL, '부산', '기장군 기장읍 기장대로 1717번길', '46020', '2023-12-16', 0, 5, '19:00:00', '16:00:00', 7),
    (21, CURRENT_TIMESTAMP(6), NULL, '대구', '달성군 다사읍 성서로 1818번길', '42926', '2023-12-17', 0, 5, '19:00:00', '16:00:00', 7);

-- 교육 프로그램 8 - 커피 블렌딩과 로스팅 교육
INSERT INTO education_place (id, created_at, updated_at, city, street, zip_code, date, current_participant, max_participant, end_time, start_time, education_id)
VALUES
    (22, CURRENT_TIMESTAMP(6), NULL, '서울', '서대문구 연세로 2020번길', '03722', '2023-12-15', 0, 3, '17:00:00', '14:00:00', 8),
    (23, CURRENT_TIMESTAMP(6), NULL, '서울', '마포구 와우산로 2121번길', '04011', '2023-12-16', 0, 3, '17:00:00', '14:00:00', 8),
    (24, CURRENT_TIMESTAMP(6), NULL, '인천', '연수구 연수로 2222번길', '22004', '2023-12-17', 0, 3, '17:00:00', '14:00:00', 8);

-- 교육 프로그램 9 - 커피 장비 사용 교육
INSERT INTO education_place (id, created_at, updated_at, city, street, zip_code, date, current_participant, max_participant, end_time, start_time, education_id)
VALUES
    (25, CURRENT_TIMESTAMP(6), NULL, '부산', '서구 보수대로 2323번길', '49108', '2023-12-15', 0, 2, '18:00:00', '15:00:00', 9),
    (26, CURRENT_TIMESTAMP(6), NULL, '부산', '동래구 온천장로 2424번길', '47891', '2023-12-16', 0, 2, '18:00:00', '15:00:00', 9),
    (27, CURRENT_TIMESTAMP(6), NULL, '대구', '달서구 달구벌대로 2525번길', '42702', '2023-12-17', 0, 2, '18:00:00', '15:00:00', 9);

-- 교육 프로그램 10 - 매장 운영과 관리 교육
INSERT INTO education_place (id, created_at, updated_at, city, street, zip_code, date, current_participant, max_participant, end_time, start_time, education_id)
VALUES
    (28, CURRENT_TIMESTAMP(6), NULL, '경기', '용인시 기흥구 용구대로 2626번길', '16977', '2023-12-15', 0, 7, '16:00:00', '13:00:00', 10),
    (29, CURRENT_TIMESTAMP(6), NULL, '경기', '성남시 분당구 서현로 2727번길', '13623', '2023-12-16', 0, 8, '16:00:00', '13:00:00', 10),
    (30, CURRENT_TIMESTAMP(6), NULL, '서울', '강서구 화곡로 2828번길', '07679', '2023-12-17', 0, 9, '16:00:00', '13:00:00', 10);

-- 매장에서 들어야할 교육 프로그램 데이터
INSERT INTO store_education_relation(id, created_at, education_id, store_id)
VALUES
    (1, CURRENT_TIMESTAMP(6), 1, 7);

INSERT INTO user_education_place_relation(id, created_at, education_place_id, user_id)
VALUES
    (1, CURRENT_TIMESTAMP(6), 7, 7),
    (2, CURRENT_TIMESTAMP(6), 8, 8),
    (3, CURRENT_TIMESTAMP(6), 9, 11),
    (4, CURRENT_TIMESTAMP(6), 7, 14),
    (5, CURRENT_TIMESTAMP(6), 8, 15),
    (6, CURRENT_TIMESTAMP(6), 9, 16);
