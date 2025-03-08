-- Удаление схемы, если она существует
DROP SCHEMA IF EXISTS public CASCADE;

-- Создание новой схемы
CREATE SCHEMA public;

-- Назначение прав на схему
GRANT USAGE ON SCHEMA public TO sysadmin;
GRANT CREATE ON SCHEMA public TO sysadmin;

-- Назначение прав на все таблицы в схеме
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO sysadmin;

-- Установка прав по умолчанию для новых объектов
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO sysadmin;