INSERT INTO roles(role_id, role_name) VALUES (1, 'ROLE_SUPERADMIN'),
                                             (2, 'ROLE_ADMIN'),
                                             (3, 'ROLE_SALES'),
                                             (4, 'ROLE_WORKSHOP'),
                                             (5, 'ROLE_VALETER') ON CONFLICT DO NOTHING;