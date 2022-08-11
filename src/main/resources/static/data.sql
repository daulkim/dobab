-- TEST DATA
insert into category values (1, '1', '분식');
insert into category values (2, '2', '한식');
insert into category values (3, '3', '일식');
insert into category values (4, '4', '중식');
insert into meal values (1, 'CONTENT 1', (SELECT TIMESTAMPADD(HOUR, 1, NOW()) FROM DUAL), '천호동', '강동구', '서울시', NOW(), 'OPEN', '저녁!!!! 1', 'USER 1', 1, NULL);
insert into meal values (2, 'CONTENT 2', (SELECT TIMESTAMPADD(HOUR, 2, NOW()) FROM DUAL), '천호동', '강동구', '서울시', NOW(), 'OPEN', '같이 저녁 드실 분 연락 주세요....!!!!!! 2', 'USER 2', 3, NULL);
insert into meal values (3, 'CONTENT 3', (SELECT TIMESTAMPADD(HOUR, 3, NOW()) FROM DUAL), '천호동', '강동구', '서울시', NOW(), 'OPEN', '같이 저녁 드실 분 연락 주세요....!!!!!! 3', 'USER 3', 2, NULL);
insert into meal values (4, 'CONTENT 4', (SELECT TIMESTAMPADD(HOUR, 1, NOW()) FROM DUAL), '천호동', '강동구', '서울시', NOW(), 'OPEN', '점심 4', 'USER 4', 1, NULL);
insert into meal values (5, 'CONTENT 5', (SELECT TIMESTAMPADD(HOUR, 2, NOW()) FROM DUAL), '천호동', '강동구', '서울시', NOW(), 'OPEN', '같이 저녁 드실 분 연락 주세요....!!!!!! 5', 'USER 5', 4, NULL);
insert into meal values (6, 'CONTENT 6', (SELECT TIMESTAMPADD(HOUR, 3, NOW()) FROM DUAL), '천호동', '강동구', '서울시', NOW(), 'OPEN', '같이 저녁 드실 분 연락 주세요....!!!!!! 6', 'USER 6', 1, NULL);
insert into meal values (7, 'CONTENT 7', (SELECT TIMESTAMPADD(HOUR, 1, NOW()) FROM DUAL), '천호동', '강동구', '서울시', NOW(), 'OPEN', '같이 저녁 드실 분 연락 주세요....!!!!!! 7', 'USER 7', 2, NULL);
insert into meal values (8, 'CONTENT 8', (SELECT TIMESTAMPADD(HOUR, 2, NOW()) FROM DUAL), '천호동', '강동구', '서울시', NOW(), 'OPEN', '같이 저녁 드실 분 연락 주세요....!!!!!! 8', 'USER 8', 1, NULL);
insert into meal values (9, 'CONTENT 9', (SELECT TIMESTAMPADD(HOUR, 3, NOW()) FROM DUAL), '천호동', '강동구', '서울시', NOW(), 'OPEN', '같이 저녁 드실 분 연락 주세요....!!!!!! 9', 'USER 9', 1, NULL);
insert into meal values (10, 'CONTENT 10', (SELECT TIMESTAMPADD(HOUR, 1, NOW()) FROM DUAL), '천호동', '강동구', '서울시', NOW(), 'OPEN', '같이 저녁 드실 분 연락 주세요....!!!!!! 10', 'USER 10', 3, NULL);
insert into meal values (11, 'CONTENT 11', (SELECT TIMESTAMPADD(HOUR, 2, NOW()) FROM DUAL), '천호동', '강동구', '서울시', NOW(), 'OPEN', '같이 저녁 드실 분 연락 주세요....!!!!!! 11', 'USER 11', 1, NULL);
insert into meal values (12, 'CONTENT 12', (SELECT TIMESTAMPADD(HOUR, 3, NOW()) FROM DUAL), '천호동', '강동구', '서울시', NOW(), 'OPEN', '같이 저녁 드실 분 연락 주세요....!!!!!! 12', 'USER 12', 1, NULL);
insert into meal values (13, 'CONTENT 1', (SELECT TIMESTAMPADD(HOUR, 1, NOW()) FROM DUAL), '천호동', '강동구', '서울시', NOW(), 'OPEN', '같이 저녁 드실 분 연락 주세요....!!!!!! 13', 'USER 1', 1, NULL);
insert into meal values (14, 'CONTENT 2', (SELECT TIMESTAMPADD(HOUR, 2, NOW()) FROM DUAL), '천호동', '강동구', '서울시', NOW(), 'OPEN', '점심....!!!!!! 14', 'USER 2', 1, NULL);
insert into meal values (15, 'CONTENT 3', (SELECT TIMESTAMPADD(HOUR, 3, NOW()) FROM DUAL), '천호동', '강동구', '서울시', NOW(), 'OPEN', '같이 저녁 드실 분 연락 주세요....!!!!!! 15', 'USER 3', 1, NULL);
insert into meal values (16, 'CONTENT 4', (SELECT TIMESTAMPADD(HOUR, 1, NOW()) FROM DUAL), '천호동', '강동구', '서울시', NOW(), 'OPEN', '같이 저녁 드실 분 연락 주세요....!!!!!! 16', 'USER 4', 4, NULL);
insert into meal values (17, 'CONTENT 5', (SELECT TIMESTAMPADD(HOUR, 2, NOW()) FROM DUAL), '천호동', '강동구', '서울시', NOW(), 'OPEN', '같이 저녁 드실 분 연락 주세요....!!!!!! 17', 'USER 5', 2, NULL);
insert into meal values (18, 'CONTENT 6', (SELECT TIMESTAMPADD(HOUR, 3, NOW()) FROM DUAL), '천호동', '강동구', '서울시', NOW(), 'OPEN', '같이 저녁....!!!!!! 18', 'USER 6', 1, NULL);
insert into meal values (19, 'CONTENT 7', (SELECT TIMESTAMPADD(HOUR, 1, NOW()) FROM DUAL), '천호동', '강동구', '서울시', NOW(), 'OPEN', '같이 저녁 드실 분 연락 주세요....!!!!!! 19', 'USER 7', 4, NULL);
insert into meal values (20, 'CONTENT 8', (SELECT TIMESTAMPADD(HOUR, 2, NOW()) FROM DUAL), '천호동', '강동구', '서울시', NOW(), 'OPEN', '같이 저녁 드실 분 연락 주세요....!!!!!! 20', 'USER 8', 1, NULL);
insert into meal values (21, 'CONTENT 9', (SELECT TIMESTAMPADD(HOUR, 3, NOW()) FROM DUAL), '천호동', '강동구', '서울시', NOW(), 'OPEN', '점심....!!!!!! 21', 'USER 9', 1, NULL);
insert into meal values (22, 'CONTENT 10', (SELECT TIMESTAMPADD(HOUR, 1, NOW()) FROM DUAL), '천호동', '강동구', '서울시', NOW(), 'OPEN', '같이 저녁 드실 분 연락 주세요....!!!!!! 22', 'USER 10', 2, NULL);
insert into meal values (23, 'CONTENT 11', (SELECT TIMESTAMPADD(HOUR, 2, NOW()) FROM DUAL), '천호동', '강동구', '서울시', NOW(), 'OPEN', '같이 저녁 드실 분 연락 주세요....!!!!!! 23', 'USER 11', 1, NULL);
insert into meal values (24, 'CONTENT 12', (SELECT TIMESTAMPADD(HOUR, 3, NOW()) FROM DUAL), '천호동', '강동구', '서울시', NOW(), 'OPEN', '같이 저녁 드실 분 연락 주세요....!!!!!! 24', 'USER 12', 3, NULL);