-- projects
--DELETE FROM `projects` WHERE `id`='000ad2e4-a785-4e1a-b082-60e71381c64a';
INSERT INTO `projects` (`id`, `name`, `status`, `description`, `deleted`, `created_by`, `created_on`, `last_modified_by`, `last_modified_on`) VALUES ('000ad2e4-a785-4e1a-b082-60e71381c64a', 'sproutfx-oauth', 'ACTIVE', NULL, 0, NULL, '2022-03-29 11:19:49', NULL, '2022-03-29 10:57:02');

-- clients
--DELETE FROM `clients` WHERE `id`='000b1e76-caab-4854-ac1f-ba0f40df64c1';
INSERT INTO `clients` (`id`, `code`, `secret`, `name`, `access_token_secret`, `access_token_validity_in_seconds`, `status`, `description`, `deleted`, `created_by`, `created_on`, `last_modified_by`, `last_modified_on`, `project_id`) VALUES ('000b1e76-caab-4854-ac1f-ba0f40df64c1', 'b605d2a8058542cd888dc547f56768bc', 'SnhtODRwQTRWZmszQ2c4bWxtMDI0SThRcEFmVEdibmE=', 'sproutfx-oauth-authorization', 'EqN3bj4EwLkOLmEb2wNWD73MeRCN0eCKsFxGQDWCyrB70OOngLbP3widMJWZtseHHSsUXqdCAJowTFg1V5yUOVykFwLiU9xP', 7200, 'ACTIVE', NULL, 0, NULL, '2022-03-29 11:19:58', NULL, '2022-03-29 11:09:31', '000ad2e4-a785-4e1a-b082-60e71381c64a');

--DELETE FROM `clients` WHERE `id`='000cd991-74fc-49a9-981e-77d89f915633';
INSERT INTO `clients` (`id`, `code`, `secret`, `name`, `access_token_secret`, `access_token_validity_in_seconds`, `status`, `description`, `deleted`, `created_by`, `created_on`, `last_modified_by`, `last_modified_on`, `project_id`) VALUES ('000cd991-74fc-49a9-981e-77d89f915633', '5e0f947f90e74061afb4f521d96b5170', 'U3F5VjlJUWpQaDN6TE40MEcwaXUxWlRuQWFwY29BMkk=', 'sproutfx-oauth-backoffice', 'sOLpFJKe58kMKDtx4gutvj4444fqoMWmngouZQLFEy4qaKSMQRToRBUcKpG7mkRupVWOQJtopjW29QRGDtnKopvSIewlAVOW', 7200, 'ACTIVE', NULL, 0, NULL, '2022-03-29 11:19:58', NULL, '2022-03-29 11:09:32', '000ad2e4-a785-4e1a-b082-60e71381c64a');

-- members
--DELETE FROM `members` WHERE `id`='0000f14c-e1d9-4908-8c0e-8abba741dd97';
INSERT INTO `members` (`id`, `email`, `name`, `password`, `password_expired`, `status`, `description`, `deleted`, `created_by`, `created_on`, `last_modified_by`, `last_modified_on`) VALUES ('0000f14c-e1d9-4908-8c0e-8abba741dd97', 'administrator@sproutfx.kr', 'Administrator', '$2a$10$w2pkVplUQdYZAALam1RKze39L1.00AIEBWrIROc4qhpJsaSfog74y', '2100-01-01 00:00:00', 'ACTIVE', NULL, 0, NULL, '2022-01-01 00:00:00', NULL, '2022-01-01 00:00:00');

--DELETE FROM `members` WHERE `id`='00017619-6e64-4701-9eea-f4dc8c9fa8d5';
INSERT INTO `members` (`id`, `email`, `name`, `password`, `password_expired`, `status`, `description`, `deleted`, `created_by`, `created_on`, `last_modified_by`, `last_modified_on`) VALUES ('00017619-6e64-4701-9eea-f4dc8c9fa8d5', 'jwseo@live.co.kr', 'Jinwon Seo', '$2a$10$l4qlIlSJPn0/u1RUOHf2I.2qdp6XXD.nyoDwN4xxC/OKSrHcrFDTi', '2100-01-01 00:00:00', 'ACTIVE', NULL, 0, NULL, '2022-01-01 00:00:00', NULL, '2022-01-01 00:00:00');

--DELETE FROM `members` WHERE `id` = '0001e564-2a07-4fd3-b69a-188678f36f3f';
INSERT INTO `members` (`id`, `email`, `name`, `password`, `password_expired`, `status`, `description`, `deleted`, `created_by`, `created_on`, `last_modified_by`, `last_modified_on`) VALUES ('0001e564-2a07-4fd3-b69a-188678f36f3f', 'test@sproutfx.kr', 'Test user', '$2a$10$t3gQckUS6jkheWvu51t7Ge/W7UTvZFotPcbYRLgEt3uCzYIxIUT7i', '2100-01-01 00:00:00', 'ACTIVE', NULL, 0, NULL, '2022-01-01 00:00:00', NULL, '2022-01-01 00:00:00');