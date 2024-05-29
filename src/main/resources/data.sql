

INSERT INTO roles(role_id, role_name) VALUES (1, 'ROLE_SUPERADMIN'),
                                             (2, 'ROLE_ADMIN'),
                                             (3, 'ROLE_USER') ON CONFLICT DO NOTHING;