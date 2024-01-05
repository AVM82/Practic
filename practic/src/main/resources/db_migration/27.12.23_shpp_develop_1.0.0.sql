--
-- PostgreSQL database dump
--

-- Dumped from database version 15.3
-- Dumped by pg_dump version 15.3

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: develop; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA develop;


ALTER SCHEMA develop OWNER TO postgres;

--
-- Name: SCHEMA develop; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON SCHEMA develop IS 'Schema for develop';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: additional_materials; Type: TABLE; Schema: develop; Owner: postgres
--

CREATE TABLE develop.additional_materials (
    number integer NOT NULL,
    course_id bigint,
    id bigint NOT NULL,
    name character varying(255)
);


ALTER TABLE develop.additional_materials OWNER TO postgres;

--
-- Name: additional_materials_refs; Type: TABLE; Schema: develop; Owner: postgres
--

CREATE TABLE develop.additional_materials_refs (
    additional_materials_entity_id bigint NOT NULL,
    refs_id bigint NOT NULL
);


ALTER TABLE develop.additional_materials_refs OWNER TO postgres;

--
-- Name: additional_materials_seq; Type: SEQUENCE; Schema: develop; Owner: postgres
--

CREATE SEQUENCE develop.additional_materials_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE develop.additional_materials_seq OWNER TO postgres;

--
-- Name: additionals; Type: TABLE; Schema: develop; Owner: postgres
--

CREATE TABLE develop.additionals (
    number integer NOT NULL,
    chapter_part_id bigint,
    id bigint NOT NULL,
    name character varying(1024)
);


ALTER TABLE develop.additionals OWNER TO postgres;

--
-- Name: additionals_refs; Type: TABLE; Schema: develop; Owner: postgres
--

CREATE TABLE develop.additionals_refs (
    additional_entity_id bigint NOT NULL,
    refs_id bigint NOT NULL
);


ALTER TABLE develop.additionals_refs OWNER TO postgres;

--
-- Name: additionals_seq; Type: SEQUENCE; Schema: develop; Owner: postgres
--

CREATE SEQUENCE develop.additionals_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE develop.additionals_seq OWNER TO postgres;

--
-- Name: answers; Type: TABLE; Schema: develop; Owner: postgres
--

CREATE TABLE develop.answers (
    correct boolean,
    id bigint NOT NULL,
    question_id bigint,
    answer character varying(255)
);


ALTER TABLE develop.answers OWNER TO postgres;

--
-- Name: answers_seq; Type: SEQUENCE; Schema: develop; Owner: postgres
--

CREATE SEQUENCE develop.answers_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE develop.answers_seq OWNER TO postgres;

--
-- Name: applicants; Type: TABLE; Schema: develop; Owner: postgres
--

CREATE TABLE develop.applicants (
    is_applied boolean DEFAULT false,
    is_rejected boolean DEFAULT false,
    course_id bigint,
    created_at timestamp(6) without time zone,
    id bigint NOT NULL,
    person_id bigint,
    student_id bigint
);


ALTER TABLE develop.applicants OWNER TO postgres;

--
-- Name: applicants_id_seq; Type: SEQUENCE; Schema: develop; Owner: postgres
--

CREATE SEQUENCE develop.applicants_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE develop.applicants_id_seq OWNER TO postgres;

--
-- Name: applicants_id_seq; Type: SEQUENCE OWNED BY; Schema: develop; Owner: postgres
--

ALTER SEQUENCE develop.applicants_id_seq OWNED BY develop.applicants.id;


--
-- Name: chapter_parts; Type: TABLE; Schema: develop; Owner: postgres
--

CREATE TABLE develop.chapter_parts (
    number integer NOT NULL,
    chapter_id bigint,
    id bigint NOT NULL,
    praxis_purpose character varying(255)
);


ALTER TABLE develop.chapter_parts OWNER TO postgres;

--
-- Name: chapter_parts_seq; Type: SEQUENCE; Schema: develop; Owner: postgres
--

CREATE SEQUENCE develop.chapter_parts_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE develop.chapter_parts_seq OWNER TO postgres;

--
-- Name: chapters; Type: TABLE; Schema: develop; Owner: postgres
--

CREATE TABLE develop.chapters (
    number integer NOT NULL,
    course_id bigint,
    id bigint NOT NULL,
    name character varying(1024),
    short_name character varying(255),
    skills character varying(255)[]
);


ALTER TABLE develop.chapters OWNER TO postgres;

--
-- Name: chapters_seq; Type: SEQUENCE; Schema: develop; Owner: postgres
--

CREATE SEQUENCE develop.chapters_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE develop.chapters_seq OWNER TO postgres;

--
-- Name: courses; Type: TABLE; Schema: develop; Owner: postgres
--

CREATE TABLE develop.courses (
    additional_materials_exist boolean NOT NULL,
    inactive boolean NOT NULL,
    id bigint NOT NULL,
    description character varying(8192),
    svg character varying(16384),
    course_type character varying(255),
    name character varying(255),
    slug character varying(255),
    authors character varying(255)[]
);


ALTER TABLE develop.courses OWNER TO postgres;

--
-- Name: courses_seq; Type: SEQUENCE; Schema: develop; Owner: postgres
--

CREATE SEQUENCE develop.courses_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE develop.courses_seq OWNER TO postgres;

--
-- Name: courses_settings; Type: TABLE; Schema: develop; Owner: postgres
--

CREATE TABLE develop.courses_settings (
    course_id bigint NOT NULL,
    id bigint NOT NULL,
    moderator_email character varying(255) NOT NULL
);


ALTER TABLE develop.courses_settings OWNER TO postgres;

--
-- Name: courses_settings_id_seq; Type: SEQUENCE; Schema: develop; Owner: postgres
--

CREATE SEQUENCE develop.courses_settings_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE develop.courses_settings_id_seq OWNER TO postgres;

--
-- Name: courses_settings_id_seq; Type: SEQUENCE OWNED BY; Schema: develop; Owner: postgres
--

ALTER SEQUENCE develop.courses_settings_id_seq OWNED BY develop.courses_settings.id;


--
-- Name: feedbacks; Type: TABLE; Schema: develop; Owner: postgres
--

CREATE TABLE develop.feedbacks (
    likes integer NOT NULL,
    date_time timestamp(6) without time zone,
    id bigint NOT NULL,
    person_id bigint NOT NULL,
    feedback character varying(320),
    name character varying(255),
    profile_picture_url character varying(255),
    liked_by_person bigint[]
);


ALTER TABLE develop.feedbacks OWNER TO postgres;

--
-- Name: feedbacks_seq; Type: SEQUENCE; Schema: develop; Owner: postgres
--

CREATE SEQUENCE develop.feedbacks_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE develop.feedbacks_seq OWNER TO postgres;

--
-- Name: graduates; Type: TABLE; Schema: develop; Owner: postgres
--

CREATE TABLE develop.graduates (
    days_spent integer NOT NULL,
    finish date,
    start date,
    weeks integer NOT NULL,
    course_id bigint,
    id bigint NOT NULL,
    person_id bigint,
    student_id bigint,
    slug character varying(255),
    skills character varying(255)[]
);


ALTER TABLE develop.graduates OWNER TO postgres;

--
-- Name: graduates_seq; Type: SEQUENCE; Schema: develop; Owner: postgres
--

CREATE SEQUENCE develop.graduates_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE develop.graduates_seq OWNER TO postgres;

--
-- Name: levels; Type: TABLE; Schema: develop; Owner: postgres
--

CREATE TABLE develop.levels (
    number integer NOT NULL,
    course_id bigint,
    id bigint NOT NULL,
    another_chat character varying(255),
    discord_chat character varying(255),
    telegram_chat character varying(255),
    chaptern integer[]
);


ALTER TABLE develop.levels OWNER TO postgres;

--
-- Name: levels_seq; Type: SEQUENCE; Schema: develop; Owner: postgres
--

CREATE SEQUENCE develop.levels_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE develop.levels_seq OWNER TO postgres;

--
-- Name: mentors; Type: TABLE; Schema: develop; Owner: postgres
--

CREATE TABLE develop.mentors (
    inactive boolean NOT NULL,
    course_id bigint,
    id bigint NOT NULL,
    person_id bigint,
    person_page_url character varying(255)
);


ALTER TABLE develop.mentors OWNER TO postgres;

--
-- Name: mentors_seq; Type: SEQUENCE; Schema: develop; Owner: postgres
--

CREATE SEQUENCE develop.mentors_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE develop.mentors_seq OWNER TO postgres;

--
-- Name: persons; Type: TABLE; Schema: develop; Owner: postgres
--

CREATE TABLE develop.persons (
    ban boolean NOT NULL,
    inactive boolean NOT NULL,
    id bigint NOT NULL,
    registered timestamp(6) without time zone,
    contacts character varying(255),
    discord character varying(255),
    email character varying(255),
    linkedin character varying(255),
    name character varying(255),
    password character varying(255),
    person_page_url character varying(255),
    profile_picture_url character varying(255)
);


ALTER TABLE develop.persons OWNER TO postgres;

--
-- Name: persons_roles; Type: TABLE; Schema: develop; Owner: postgres
--

CREATE TABLE develop.persons_roles (
    person_id bigint NOT NULL,
    role_id bigint NOT NULL
);


ALTER TABLE develop.persons_roles OWNER TO postgres;

--
-- Name: persons_seq; Type: SEQUENCE; Schema: develop; Owner: postgres
--

CREATE SEQUENCE develop.persons_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE develop.persons_seq OWNER TO postgres;

--
-- Name: practices; Type: TABLE; Schema: develop; Owner: postgres
--

CREATE TABLE develop.practices (
    days_spent integer NOT NULL,
    number integer NOT NULL,
    start_counting date,
    created_at timestamp(6) without time zone NOT NULL,
    id bigint NOT NULL,
    student_chapter_id bigint,
    updated_at timestamp(6) without time zone,
    state character varying(255),
    CONSTRAINT practices_state_check CHECK (((state)::text = ANY ((ARRAY['NOT_STARTED'::character varying, 'IN_PROCESS'::character varying, 'PAUSE'::character varying, 'READY_TO_REVIEW'::character varying, 'APPROVED'::character varying])::text[])))
);


ALTER TABLE develop.practices OWNER TO postgres;

--
-- Name: practices_seq; Type: SEQUENCE; Schema: develop; Owner: postgres
--

CREATE SEQUENCE develop.practices_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE develop.practices_seq OWNER TO postgres;

--
-- Name: praxis; Type: TABLE; Schema: develop; Owner: postgres
--

CREATE TABLE develop.praxis (
    number integer NOT NULL,
    chapter_part_id bigint,
    id bigint NOT NULL,
    name character varying(1024)
);


ALTER TABLE develop.praxis OWNER TO postgres;

--
-- Name: praxis_refs; Type: TABLE; Schema: develop; Owner: postgres
--

CREATE TABLE develop.praxis_refs (
    praxis_entity_id bigint NOT NULL,
    refs_id bigint NOT NULL
);


ALTER TABLE develop.praxis_refs OWNER TO postgres;

--
-- Name: praxis_seq; Type: SEQUENCE; Schema: develop; Owner: postgres
--

CREATE SEQUENCE develop.praxis_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE develop.praxis_seq OWNER TO postgres;

--
-- Name: pre_verification_user; Type: TABLE; Schema: develop; Owner: postgres
--

CREATE TABLE develop.pre_verification_user (
    expired_at timestamp(6) without time zone NOT NULL,
    id bigint NOT NULL,
    email character varying(255),
    name character varying(255),
    password character varying(255),
    token character varying(255)
);


ALTER TABLE develop.pre_verification_user OWNER TO postgres;

--
-- Name: pre_verification_user_seq; Type: SEQUENCE; Schema: develop; Owner: postgres
--

CREATE SEQUENCE develop.pre_verification_user_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE develop.pre_verification_user_seq OWNER TO postgres;

--
-- Name: profile; Type: TABLE; Schema: develop; Owner: postgres
--

CREATE TABLE develop.profile (
    notification_discord boolean NOT NULL,
    notification_email boolean NOT NULL,
    id bigint NOT NULL,
    person_id bigint,
    updated_at timestamp(6) without time zone,
    city character varying(255),
    country character varying(255)
);


ALTER TABLE develop.profile OWNER TO postgres;

--
-- Name: profile_seq; Type: SEQUENCE; Schema: develop; Owner: postgres
--

CREATE SEQUENCE develop.profile_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE develop.profile_seq OWNER TO postgres;

--
-- Name: questions; Type: TABLE; Schema: develop; Owner: postgres
--

CREATE TABLE develop.questions (
    number integer NOT NULL,
    id bigint NOT NULL,
    quiz_id bigint,
    question character varying(255)
);


ALTER TABLE develop.questions OWNER TO postgres;

--
-- Name: questions_seq; Type: SEQUENCE; Schema: develop; Owner: postgres
--

CREATE SEQUENCE develop.questions_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE develop.questions_seq OWNER TO postgres;

--
-- Name: quiz_results; Type: TABLE; Schema: develop; Owner: postgres
--

CREATE TABLE develop.quiz_results (
    answered_count integer NOT NULL,
    correct_answered_count integer NOT NULL,
    passed boolean NOT NULL,
    question_count integer NOT NULL,
    id bigint NOT NULL,
    second_spent bigint NOT NULL,
    started_at timestamp(6) without time zone,
    student_chapter_id bigint
);


ALTER TABLE develop.quiz_results OWNER TO postgres;

--
-- Name: quiz_results_seq; Type: SEQUENCE; Schema: develop; Owner: postgres
--

CREATE SEQUENCE develop.quiz_results_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE develop.quiz_results_seq OWNER TO postgres;

--
-- Name: quizes; Type: TABLE; Schema: develop; Owner: postgres
--

CREATE TABLE develop.quizes (
    chapter_id bigint,
    id bigint NOT NULL
);


ALTER TABLE develop.quizes OWNER TO postgres;

--
-- Name: quizes_seq; Type: SEQUENCE; Schema: develop; Owner: postgres
--

CREATE SEQUENCE develop.quizes_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE develop.quizes_seq OWNER TO postgres;

--
-- Name: reference_title; Type: TABLE; Schema: develop; Owner: postgres
--

CREATE TABLE develop.reference_title (
    id bigint NOT NULL,
    reference character varying(255),
    title character varying(255)
);


ALTER TABLE develop.reference_title OWNER TO postgres;

--
-- Name: reference_title_seq; Type: SEQUENCE; Schema: develop; Owner: postgres
--

CREATE SEQUENCE develop.reference_title_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE develop.reference_title_seq OWNER TO postgres;

--
-- Name: report_timeslots; Type: TABLE; Schema: develop; Owner: postgres
--

CREATE TABLE develop.report_timeslots (
    availability boolean NOT NULL,
    date date NOT NULL,
    "time" time(6) without time zone NOT NULL,
    id bigint NOT NULL
);


ALTER TABLE develop.report_timeslots OWNER TO postgres;

--
-- Name: report_timeslots_seq; Type: SEQUENCE; Schema: develop; Owner: postgres
--

CREATE SEQUENCE develop.report_timeslots_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE develop.report_timeslots_seq OWNER TO postgres;

--
-- Name: reports; Type: TABLE; Schema: develop; Owner: postgres
--

CREATE TABLE develop.reports (
    id bigint NOT NULL,
    chapter_number integer NOT NULL,
    date date,
    duration numeric(21,0),
    liked_person_ids bigint[],
    start timestamp(6) without time zone,
    state character varying(255),
    course_id bigint,
    person_id bigint,
    student_chapter_id bigint,
    topic_id bigint,
    CONSTRAINT reports_state_check CHECK (((state)::text = ANY ((ARRAY['CANCELLED'::character varying, 'APPROVED'::character varying, 'FINISHED'::character varying, 'STARTED'::character varying, 'ANNOUNCED'::character varying])::text[])))
);


ALTER TABLE develop.reports OWNER TO postgres;

--
-- Name: reports_seq; Type: SEQUENCE; Schema: develop; Owner: postgres
--

CREATE SEQUENCE develop.reports_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE develop.reports_seq OWNER TO postgres;

--
-- Name: reset_code; Type: TABLE; Schema: develop; Owner: postgres
--

CREATE TABLE develop.reset_code (
    expired_at timestamp(6) without time zone,
    id bigint NOT NULL,
    code character varying(255),
    email character varying(255)
);


ALTER TABLE develop.reset_code OWNER TO postgres;

--
-- Name: reset_code_seq; Type: SEQUENCE; Schema: develop; Owner: postgres
--

CREATE SEQUENCE develop.reset_code_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE develop.reset_code_seq OWNER TO postgres;

--
-- Name: roles; Type: TABLE; Schema: develop; Owner: postgres
--

CREATE TABLE develop.roles (
    id bigint NOT NULL,
    name character varying(255)
);


ALTER TABLE develop.roles OWNER TO postgres;

--
-- Name: roles_seq; Type: SEQUENCE; Schema: develop; Owner: postgres
--

CREATE SEQUENCE develop.roles_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE develop.roles_seq OWNER TO postgres;

--
-- Name: skills; Type: TABLE; Schema: develop; Owner: postgres
--

CREATE TABLE develop.skills (
    id bigint NOT NULL,
    name character varying(255)
);


ALTER TABLE develop.skills OWNER TO postgres;

--
-- Name: skills_seq; Type: SEQUENCE; Schema: develop; Owner: postgres
--

CREATE SEQUENCE develop.skills_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE develop.skills_seq OWNER TO postgres;

--
-- Name: statistic_course; Type: TABLE; Schema: develop; Owner: postgres
--

CREATE TABLE develop.statistic_course (
    applicant_applied_count bigint NOT NULL,
    applicant_rejected_count bigint NOT NULL,
    applicant_total_count bigint NOT NULL,
    course_id bigint,
    created_at timestamp(6) without time zone NOT NULL,
    graduate_count bigint NOT NULL,
    id bigint NOT NULL,
    student_banned_count bigint NOT NULL,
    student_finished_count bigint NOT NULL,
    student_started_count bigint NOT NULL,
    updated_at timestamp(6) without time zone
);


ALTER TABLE develop.statistic_course OWNER TO postgres;

--
-- Name: statistic_course_seq; Type: SEQUENCE; Schema: develop; Owner: postgres
--

CREATE SEQUENCE develop.statistic_course_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE develop.statistic_course_seq OWNER TO postgres;

--
-- Name: statistic_student_chapters; Type: TABLE; Schema: develop; Owner: postgres
--

CREATE TABLE develop.statistic_student_chapters (
    chapter_id bigint,
    course_id bigint,
    created_at timestamp(6) without time zone NOT NULL,
    day_count bigint NOT NULL,
    id bigint NOT NULL,
    quiz_passed_count bigint NOT NULL,
    quiz_passed_result bigint NOT NULL,
    quiz_total_count bigint NOT NULL,
    quiz_total_result bigint NOT NULL,
    report_count bigint NOT NULL,
    student_chapter_count bigint NOT NULL,
    updated_at timestamp(6) without time zone,
    practice_day_count bigint[]
);


ALTER TABLE develop.statistic_student_chapters OWNER TO postgres;

--
-- Name: statistic_student_chapters_seq; Type: SEQUENCE; Schema: develop; Owner: postgres
--

CREATE SEQUENCE develop.statistic_student_chapters_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE develop.statistic_student_chapters_seq OWNER TO postgres;

--
-- Name: student_chapters; Type: TABLE; Schema: develop; Owner: postgres
--

CREATE TABLE develop.student_chapters (
    days_spent integer NOT NULL,
    is_quiz_passed boolean,
    number integer NOT NULL,
    start_counting date,
    chapter_id bigint,
    created_at timestamp(6) without time zone NOT NULL,
    id bigint NOT NULL,
    student_id bigint,
    updated_at timestamp(6) without time zone,
    state character varying(255),
    subs bigint[],
    CONSTRAINT student_chapters_state_check CHECK (((state)::text = ANY ((ARRAY['NOT_STARTED'::character varying, 'IN_PROCESS'::character varying, 'PAUSE'::character varying, 'DONE'::character varying])::text[])))
);


ALTER TABLE develop.student_chapters OWNER TO postgres;

--
-- Name: student_chapters_seq; Type: SEQUENCE; Schema: develop; Owner: postgres
--

CREATE SEQUENCE develop.student_chapters_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE develop.student_chapters_seq OWNER TO postgres;

--
-- Name: student_reports; Type: TABLE; Schema: develop; Owner: postgres
--

CREATE TABLE develop.student_reports (
    id bigint NOT NULL,
    student_chapter_id bigint,
    time_slot_id bigint NOT NULL,
    state character varying(255),
    title character varying(255),
    liked_persons_id_list bigint[],
    CONSTRAINT student_reports_state_check CHECK (((state)::text = ANY ((ARRAY['CANCELLED'::character varying, 'APPROVED'::character varying, 'FINISHED'::character varying, 'STARTED'::character varying, 'ANNOUNCED'::character varying])::text[])))
);


ALTER TABLE develop.student_reports OWNER TO postgres;

--
-- Name: student_reports_seq; Type: SEQUENCE; Schema: develop; Owner: postgres
--

CREATE SEQUENCE develop.student_reports_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE develop.student_reports_seq OWNER TO postgres;

--
-- Name: students; Type: TABLE; Schema: develop; Owner: postgres
--

CREATE TABLE develop.students (
    active_chapter_number integer NOT NULL,
    ban boolean NOT NULL,
    days_spent integer NOT NULL,
    finish date,
    inactive boolean NOT NULL,
    registered date,
    start date,
    weeks integer NOT NULL,
    course_id bigint,
    id bigint NOT NULL,
    person_id bigint,
    english character varying(255),
    os character varying(255),
    purpose character varying(255),
    skills character varying(255)[]
);


ALTER TABLE develop.students OWNER TO postgres;

--
-- Name: students_seq; Type: SEQUENCE; Schema: develop; Owner: postgres
--

CREATE SEQUENCE develop.students_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE develop.students_seq OWNER TO postgres;

--
-- Name: students_student_add_mats; Type: TABLE; Schema: develop; Owner: postgres
--

CREATE TABLE develop.students_student_add_mats (
    add_mat_id bigint NOT NULL,
    student_id bigint NOT NULL
);


ALTER TABLE develop.students_student_add_mats OWNER TO postgres;

--
-- Name: sub_chapters; Type: TABLE; Schema: develop; Owner: postgres
--

CREATE TABLE develop.sub_chapters (
    number integer NOT NULL,
    chapter_part_id bigint,
    id bigint NOT NULL,
    name character varying(1024),
    skills character varying(255)[]
);


ALTER TABLE develop.sub_chapters OWNER TO postgres;

--
-- Name: sub_chapters_refs; Type: TABLE; Schema: develop; Owner: postgres
--

CREATE TABLE develop.sub_chapters_refs (
    refs_id bigint NOT NULL,
    sub_chapter_entity_id bigint NOT NULL
);


ALTER TABLE develop.sub_chapters_refs OWNER TO postgres;

--
-- Name: sub_chapters_seq; Type: SEQUENCE; Schema: develop; Owner: postgres
--

CREATE SEQUENCE develop.sub_chapters_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE develop.sub_chapters_seq OWNER TO postgres;

--
-- Name: sub_chapters_skills; Type: TABLE; Schema: develop; Owner: postgres
--

CREATE TABLE develop.sub_chapters_skills (
    skill_id bigint NOT NULL,
    sub_chapter_id bigint NOT NULL
);


ALTER TABLE develop.sub_chapters_skills OWNER TO postgres;

--
-- Name: sub_sub_chapters; Type: TABLE; Schema: develop; Owner: postgres
--

CREATE TABLE develop.sub_sub_chapters (
    number integer NOT NULL,
    id bigint NOT NULL,
    sub_chapter_id bigint,
    name character varying(1024)
);


ALTER TABLE develop.sub_sub_chapters OWNER TO postgres;

--
-- Name: sub_sub_chapters_refs; Type: TABLE; Schema: develop; Owner: postgres
--

CREATE TABLE develop.sub_sub_chapters_refs (
    refs_id bigint NOT NULL,
    sub_sub_chapter_entity_id bigint NOT NULL
);


ALTER TABLE develop.sub_sub_chapters_refs OWNER TO postgres;

--
-- Name: sub_sub_chapters_seq; Type: SEQUENCE; Schema: develop; Owner: postgres
--

CREATE SEQUENCE develop.sub_sub_chapters_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE develop.sub_sub_chapters_seq OWNER TO postgres;

--
-- Name: topic_reports; Type: TABLE; Schema: develop; Owner: postgres
--

CREATE TABLE develop.topic_reports (
    chapter_id bigint,
    id bigint NOT NULL,
    topic character varying(255)
);


ALTER TABLE develop.topic_reports OWNER TO postgres;

--
-- Name: topic_reports_seq; Type: SEQUENCE; Schema: develop; Owner: postgres
--

CREATE SEQUENCE develop.topic_reports_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE develop.topic_reports_seq OWNER TO postgres;

--
-- Name: applicants id; Type: DEFAULT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.applicants ALTER COLUMN id SET DEFAULT nextval('develop.applicants_id_seq'::regclass);


--
-- Name: courses_settings id; Type: DEFAULT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.courses_settings ALTER COLUMN id SET DEFAULT nextval('develop.courses_settings_id_seq'::regclass);


--
-- Name: additional_materials_seq; Type: SEQUENCE SET; Schema: develop; Owner: postgres
--

SELECT pg_catalog.setval('develop.additional_materials_seq', 51, true);


--
-- Name: additionals_seq; Type: SEQUENCE SET; Schema: develop; Owner: postgres
--

SELECT pg_catalog.setval('develop.additionals_seq', 51, true);


--
-- Name: answers_seq; Type: SEQUENCE SET; Schema: develop; Owner: postgres
--

SELECT pg_catalog.setval('develop.answers_seq', 1, false);


--
-- Name: applicants_id_seq; Type: SEQUENCE SET; Schema: develop; Owner: postgres
--

SELECT pg_catalog.setval('develop.applicants_id_seq', 1, false);


--
-- Name: chapter_parts_seq; Type: SEQUENCE SET; Schema: develop; Owner: postgres
--

SELECT pg_catalog.setval('develop.chapter_parts_seq', 51, true);


--
-- Name: chapters_seq; Type: SEQUENCE SET; Schema: develop; Owner: postgres
--

SELECT pg_catalog.setval('develop.chapters_seq', 51, true);


--
-- Name: courses_seq; Type: SEQUENCE SET; Schema: develop; Owner: postgres
--

SELECT pg_catalog.setval('develop.courses_seq', 1, true);


--
-- Name: courses_settings_id_seq; Type: SEQUENCE SET; Schema: develop; Owner: postgres
--

SELECT pg_catalog.setval('develop.courses_settings_id_seq', 1, false);


--
-- Name: feedbacks_seq; Type: SEQUENCE SET; Schema: develop; Owner: postgres
--

SELECT pg_catalog.setval('develop.feedbacks_seq', 1, false);


--
-- Name: graduates_seq; Type: SEQUENCE SET; Schema: develop; Owner: postgres
--

SELECT pg_catalog.setval('develop.graduates_seq', 1, false);


--
-- Name: levels_seq; Type: SEQUENCE SET; Schema: develop; Owner: postgres
--

SELECT pg_catalog.setval('develop.levels_seq', 51, true);


--
-- Name: mentors_seq; Type: SEQUENCE SET; Schema: develop; Owner: postgres
--

SELECT pg_catalog.setval('develop.mentors_seq', 1, false);


--
-- Name: persons_seq; Type: SEQUENCE SET; Schema: develop; Owner: postgres
--

SELECT pg_catalog.setval('develop.persons_seq', 51, true);


--
-- Name: practices_seq; Type: SEQUENCE SET; Schema: develop; Owner: postgres
--

SELECT pg_catalog.setval('develop.practices_seq', 1, false);


--
-- Name: praxis_seq; Type: SEQUENCE SET; Schema: develop; Owner: postgres
--

SELECT pg_catalog.setval('develop.praxis_seq', 51, true);


--
-- Name: pre_verification_user_seq; Type: SEQUENCE SET; Schema: develop; Owner: postgres
--

SELECT pg_catalog.setval('develop.pre_verification_user_seq', 1, false);


--
-- Name: profile_seq; Type: SEQUENCE SET; Schema: develop; Owner: postgres
--

SELECT pg_catalog.setval('develop.profile_seq', 1, false);


--
-- Name: questions_seq; Type: SEQUENCE SET; Schema: develop; Owner: postgres
--

SELECT pg_catalog.setval('develop.questions_seq', 1, false);


--
-- Name: quiz_results_seq; Type: SEQUENCE SET; Schema: develop; Owner: postgres
--

SELECT pg_catalog.setval('develop.quiz_results_seq', 1, false);


--
-- Name: quizes_seq; Type: SEQUENCE SET; Schema: develop; Owner: postgres
--

SELECT pg_catalog.setval('develop.quizes_seq', 1, false);


--
-- Name: reference_title_seq; Type: SEQUENCE SET; Schema: develop; Owner: postgres
--

SELECT pg_catalog.setval('develop.reference_title_seq', 301, true);


--
-- Name: report_timeslots_seq; Type: SEQUENCE SET; Schema: develop; Owner: postgres
--

SELECT pg_catalog.setval('develop.report_timeslots_seq', 1, false);


--
-- Name: reports_seq; Type: SEQUENCE SET; Schema: develop; Owner: postgres
--

SELECT pg_catalog.setval('develop.reports_seq', 1, false);


--
-- Name: reset_code_seq; Type: SEQUENCE SET; Schema: develop; Owner: postgres
--

SELECT pg_catalog.setval('develop.reset_code_seq', 1, false);


--
-- Name: roles_seq; Type: SEQUENCE SET; Schema: develop; Owner: postgres
--

SELECT pg_catalog.setval('develop.roles_seq', 51, true);


--
-- Name: skills_seq; Type: SEQUENCE SET; Schema: develop; Owner: postgres
--

SELECT pg_catalog.setval('develop.skills_seq', 1, false);


--
-- Name: statistic_course_seq; Type: SEQUENCE SET; Schema: develop; Owner: postgres
--

SELECT pg_catalog.setval('develop.statistic_course_seq', 1, false);


--
-- Name: statistic_student_chapters_seq; Type: SEQUENCE SET; Schema: develop; Owner: postgres
--

SELECT pg_catalog.setval('develop.statistic_student_chapters_seq', 1, false);


--
-- Name: student_chapters_seq; Type: SEQUENCE SET; Schema: develop; Owner: postgres
--

SELECT pg_catalog.setval('develop.student_chapters_seq', 1, false);


--
-- Name: student_reports_seq; Type: SEQUENCE SET; Schema: develop; Owner: postgres
--

SELECT pg_catalog.setval('develop.student_reports_seq', 1, false);


--
-- Name: students_seq; Type: SEQUENCE SET; Schema: develop; Owner: postgres
--

SELECT pg_catalog.setval('develop.students_seq', 1, false);


--
-- Name: sub_chapters_seq; Type: SEQUENCE SET; Schema: develop; Owner: postgres
--

SELECT pg_catalog.setval('develop.sub_chapters_seq', 101, true);


--
-- Name: sub_sub_chapters_seq; Type: SEQUENCE SET; Schema: develop; Owner: postgres
--

SELECT pg_catalog.setval('develop.sub_sub_chapters_seq', 101, true);


--
-- Name: topic_reports_seq; Type: SEQUENCE SET; Schema: develop; Owner: postgres
--

SELECT pg_catalog.setval('develop.topic_reports_seq', 51, true);


--
-- Name: additional_materials additional_materials_pkey; Type: CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.additional_materials
    ADD CONSTRAINT additional_materials_pkey PRIMARY KEY (id);


--
-- Name: additional_materials_refs additional_materials_refs_pkey; Type: CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.additional_materials_refs
    ADD CONSTRAINT additional_materials_refs_pkey PRIMARY KEY (additional_materials_entity_id, refs_id);


--
-- Name: additionals additionals_pkey; Type: CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.additionals
    ADD CONSTRAINT additionals_pkey PRIMARY KEY (id);


--
-- Name: additionals_refs additionals_refs_pkey; Type: CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.additionals_refs
    ADD CONSTRAINT additionals_refs_pkey PRIMARY KEY (additional_entity_id, refs_id);


--
-- Name: answers answers_pkey; Type: CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.answers
    ADD CONSTRAINT answers_pkey PRIMARY KEY (id);


--
-- Name: applicants applicants_pkey; Type: CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.applicants
    ADD CONSTRAINT applicants_pkey PRIMARY KEY (id);


--
-- Name: applicants applicants_student_id_key; Type: CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.applicants
    ADD CONSTRAINT applicants_student_id_key UNIQUE (student_id);


--
-- Name: chapter_parts chapter_parts_pkey; Type: CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.chapter_parts
    ADD CONSTRAINT chapter_parts_pkey PRIMARY KEY (id);


--
-- Name: chapters chapters_pkey; Type: CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.chapters
    ADD CONSTRAINT chapters_pkey PRIMARY KEY (id);


--
-- Name: courses courses_pkey; Type: CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.courses
    ADD CONSTRAINT courses_pkey PRIMARY KEY (id);


--
-- Name: courses_settings courses_settings_course_id_key; Type: CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.courses_settings
    ADD CONSTRAINT courses_settings_course_id_key UNIQUE (course_id);


--
-- Name: courses_settings courses_settings_pkey; Type: CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.courses_settings
    ADD CONSTRAINT courses_settings_pkey PRIMARY KEY (id);


--
-- Name: courses courses_slug_key; Type: CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.courses
    ADD CONSTRAINT courses_slug_key UNIQUE (slug);


--
-- Name: feedbacks feedbacks_pkey; Type: CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.feedbacks
    ADD CONSTRAINT feedbacks_pkey PRIMARY KEY (id);


--
-- Name: graduates graduates_pkey; Type: CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.graduates
    ADD CONSTRAINT graduates_pkey PRIMARY KEY (id);


--
-- Name: graduates graduates_student_id_key; Type: CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.graduates
    ADD CONSTRAINT graduates_student_id_key UNIQUE (student_id);


--
-- Name: levels levels_pkey; Type: CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.levels
    ADD CONSTRAINT levels_pkey PRIMARY KEY (id);


--
-- Name: mentors mentors_pkey; Type: CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.mentors
    ADD CONSTRAINT mentors_pkey PRIMARY KEY (id);


--
-- Name: persons persons_pkey; Type: CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.persons
    ADD CONSTRAINT persons_pkey PRIMARY KEY (id);


--
-- Name: persons_roles persons_roles_pkey; Type: CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.persons_roles
    ADD CONSTRAINT persons_roles_pkey PRIMARY KEY (person_id, role_id);


--
-- Name: practices practices_pkey; Type: CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.practices
    ADD CONSTRAINT practices_pkey PRIMARY KEY (id);


--
-- Name: praxis praxis_pkey; Type: CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.praxis
    ADD CONSTRAINT praxis_pkey PRIMARY KEY (id);


--
-- Name: praxis_refs praxis_refs_pkey; Type: CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.praxis_refs
    ADD CONSTRAINT praxis_refs_pkey PRIMARY KEY (praxis_entity_id, refs_id);


--
-- Name: pre_verification_user pre_verification_user_pkey; Type: CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.pre_verification_user
    ADD CONSTRAINT pre_verification_user_pkey PRIMARY KEY (id);


--
-- Name: profile profile_person_id_key; Type: CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.profile
    ADD CONSTRAINT profile_person_id_key UNIQUE (person_id);


--
-- Name: profile profile_pkey; Type: CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.profile
    ADD CONSTRAINT profile_pkey PRIMARY KEY (id);


--
-- Name: questions questions_pkey; Type: CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.questions
    ADD CONSTRAINT questions_pkey PRIMARY KEY (id);


--
-- Name: quiz_results quiz_results_pkey; Type: CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.quiz_results
    ADD CONSTRAINT quiz_results_pkey PRIMARY KEY (id);


--
-- Name: quizes quizes_chapter_id_key; Type: CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.quizes
    ADD CONSTRAINT quizes_chapter_id_key UNIQUE (chapter_id);


--
-- Name: quizes quizes_pkey; Type: CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.quizes
    ADD CONSTRAINT quizes_pkey PRIMARY KEY (id);


--
-- Name: reference_title reference_title_pkey; Type: CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.reference_title
    ADD CONSTRAINT reference_title_pkey PRIMARY KEY (id);


--
-- Name: reference_title reference_title_reference_key; Type: CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.reference_title
    ADD CONSTRAINT reference_title_reference_key UNIQUE (reference);


--
-- Name: report_timeslots report_timeslots_pkey; Type: CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.report_timeslots
    ADD CONSTRAINT report_timeslots_pkey PRIMARY KEY (id);


--
-- Name: reports reports_pkey; Type: CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.reports
    ADD CONSTRAINT reports_pkey PRIMARY KEY (id);


--
-- Name: reset_code reset_code_pkey; Type: CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.reset_code
    ADD CONSTRAINT reset_code_pkey PRIMARY KEY (id);


--
-- Name: roles roles_pkey; Type: CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (id);


--
-- Name: skills skills_pkey; Type: CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.skills
    ADD CONSTRAINT skills_pkey PRIMARY KEY (id);


--
-- Name: statistic_course statistic_course_course_id_key; Type: CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.statistic_course
    ADD CONSTRAINT statistic_course_course_id_key UNIQUE (course_id);


--
-- Name: statistic_course statistic_course_pkey; Type: CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.statistic_course
    ADD CONSTRAINT statistic_course_pkey PRIMARY KEY (id);


--
-- Name: statistic_student_chapters statistic_student_chapters_pkey; Type: CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.statistic_student_chapters
    ADD CONSTRAINT statistic_student_chapters_pkey PRIMARY KEY (id);


--
-- Name: student_chapters student_chapters_pkey; Type: CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.student_chapters
    ADD CONSTRAINT student_chapters_pkey PRIMARY KEY (id);


--
-- Name: student_reports student_reports_pkey; Type: CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.student_reports
    ADD CONSTRAINT student_reports_pkey PRIMARY KEY (id);


--
-- Name: student_reports student_reports_time_slot_id_key; Type: CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.student_reports
    ADD CONSTRAINT student_reports_time_slot_id_key UNIQUE (time_slot_id);


--
-- Name: students students_pkey; Type: CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.students
    ADD CONSTRAINT students_pkey PRIMARY KEY (id);


--
-- Name: students_student_add_mats students_student_add_mats_add_mat_id_key; Type: CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.students_student_add_mats
    ADD CONSTRAINT students_student_add_mats_add_mat_id_key UNIQUE (add_mat_id);


--
-- Name: sub_chapters sub_chapters_pkey; Type: CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.sub_chapters
    ADD CONSTRAINT sub_chapters_pkey PRIMARY KEY (id);


--
-- Name: sub_chapters_refs sub_chapters_refs_pkey; Type: CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.sub_chapters_refs
    ADD CONSTRAINT sub_chapters_refs_pkey PRIMARY KEY (refs_id, sub_chapter_entity_id);


--
-- Name: sub_sub_chapters sub_sub_chapters_pkey; Type: CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.sub_sub_chapters
    ADD CONSTRAINT sub_sub_chapters_pkey PRIMARY KEY (id);


--
-- Name: sub_sub_chapters_refs sub_sub_chapters_refs_pkey; Type: CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.sub_sub_chapters_refs
    ADD CONSTRAINT sub_sub_chapters_refs_pkey PRIMARY KEY (refs_id, sub_sub_chapter_entity_id);


--
-- Name: topic_reports topic_reports_pkey; Type: CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.topic_reports
    ADD CONSTRAINT topic_reports_pkey PRIMARY KEY (id);


--
-- Name: reference_title uk28y2p37qqwy835w0kybtekcxq; Type: CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.reference_title
    ADD CONSTRAINT uk28y2p37qqwy835w0kybtekcxq UNIQUE (reference);


--
-- Name: persons ukqrhve9imkba1unxbn2gg0iqe6; Type: CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.persons
    ADD CONSTRAINT ukqrhve9imkba1unxbn2gg0iqe6 UNIQUE (discord, email);


--
-- Name: students uniquestudentoncourse; Type: CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.students
    ADD CONSTRAINT uniquestudentoncourse UNIQUE (course_id, person_id);


--
-- Name: idx1x5aosta48fbss4d5b3kuu0rd; Type: INDEX; Schema: develop; Owner: postgres
--

CREATE INDEX idx1x5aosta48fbss4d5b3kuu0rd ON develop.persons USING btree (email);


--
-- Name: courses_settings fk27uxla7nu8mw7tdmag1vor3l3; Type: FK CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.courses_settings
    ADD CONSTRAINT fk27uxla7nu8mw7tdmag1vor3l3 FOREIGN KEY (course_id) REFERENCES develop.courses(id);


--
-- Name: statistic_student_chapters fk2wftu02m91xsb8a9epfrdf032; Type: FK CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.statistic_student_chapters
    ADD CONSTRAINT fk2wftu02m91xsb8a9epfrdf032 FOREIGN KEY (chapter_id) REFERENCES develop.chapters(id);


--
-- Name: topic_reports fk32jqtt6guqww000ane6bq38kk; Type: FK CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.topic_reports
    ADD CONSTRAINT fk32jqtt6guqww000ane6bq38kk FOREIGN KEY (chapter_id) REFERENCES develop.chapters(id);


--
-- Name: answers fk3erw1a3t0r78st8ty27x6v3g1; Type: FK CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.answers
    ADD CONSTRAINT fk3erw1a3t0r78st8ty27x6v3g1 FOREIGN KEY (question_id) REFERENCES develop.questions(id);


--
-- Name: quiz_results fk3t4xcdqcepfew24eded535r22; Type: FK CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.quiz_results
    ADD CONSTRAINT fk3t4xcdqcepfew24eded535r22 FOREIGN KEY (student_chapter_id) REFERENCES develop.student_chapters(id);


--
-- Name: students_student_add_mats fk3x92q2a8ih93ummvvvol14ivq; Type: FK CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.students_student_add_mats
    ADD CONSTRAINT fk3x92q2a8ih93ummvvvol14ivq FOREIGN KEY (add_mat_id) REFERENCES develop.additional_materials(id);


--
-- Name: additional_materials fk4x59tyta7mdmxv5u2wdm5i6if; Type: FK CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.additional_materials
    ADD CONSTRAINT fk4x59tyta7mdmxv5u2wdm5i6if FOREIGN KEY (course_id) REFERENCES develop.courses(id);


--
-- Name: statistic_course fk5o15mkciig3b73ff7p9gdpvl1; Type: FK CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.statistic_course
    ADD CONSTRAINT fk5o15mkciig3b73ff7p9gdpvl1 FOREIGN KEY (course_id) REFERENCES develop.courses(id);


--
-- Name: additional_materials_refs fk68ee599ju3qt9akwch862elco; Type: FK CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.additional_materials_refs
    ADD CONSTRAINT fk68ee599ju3qt9akwch862elco FOREIGN KEY (additional_materials_entity_id) REFERENCES develop.additional_materials(id);


--
-- Name: applicants fk6gn67smss7bxrrbxw7fi4ffpf; Type: FK CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.applicants
    ADD CONSTRAINT fk6gn67smss7bxrrbxw7fi4ffpf FOREIGN KEY (course_id) REFERENCES develop.courses(id);


--
-- Name: chapters fk6h1m0nrtdwj37570c0sp2tdcs; Type: FK CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.chapters
    ADD CONSTRAINT fk6h1m0nrtdwj37570c0sp2tdcs FOREIGN KEY (course_id) REFERENCES develop.courses(id);


--
-- Name: students fk6jiqckumc6tm0h9otqbtqhgnr; Type: FK CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.students
    ADD CONSTRAINT fk6jiqckumc6tm0h9otqbtqhgnr FOREIGN KEY (course_id) REFERENCES develop.courses(id);


--
-- Name: reports fk73tlklfjcj1uvmj61sqkbs71a; Type: FK CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.reports
    ADD CONSTRAINT fk73tlklfjcj1uvmj61sqkbs71a FOREIGN KEY (topic_id) REFERENCES develop.topic_reports(id);


--
-- Name: students fk7bj6li9e77inyp4eu1d2l0mhl; Type: FK CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.students
    ADD CONSTRAINT fk7bj6li9e77inyp4eu1d2l0mhl FOREIGN KEY (person_id) REFERENCES develop.persons(id);


--
-- Name: reports fk7j2da1k54ggn9pi8r6aw28mte; Type: FK CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.reports
    ADD CONSTRAINT fk7j2da1k54ggn9pi8r6aw28mte FOREIGN KEY (student_chapter_id) REFERENCES develop.student_chapters(id);


--
-- Name: additionals fk7t3r6mt9qthv6p2g8ojeekyrh; Type: FK CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.additionals
    ADD CONSTRAINT fk7t3r6mt9qthv6p2g8ojeekyrh FOREIGN KEY (chapter_part_id) REFERENCES develop.chapter_parts(id);


--
-- Name: student_chapters fk9mhl52v6vproo7wqmh7stvdtx; Type: FK CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.student_chapters
    ADD CONSTRAINT fk9mhl52v6vproo7wqmh7stvdtx FOREIGN KEY (chapter_id) REFERENCES develop.chapters(id);


--
-- Name: praxis fk9nxbx3a7f83gw3t0y6qm60sk9; Type: FK CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.praxis
    ADD CONSTRAINT fk9nxbx3a7f83gw3t0y6qm60sk9 FOREIGN KEY (chapter_part_id) REFERENCES develop.chapter_parts(id);


--
-- Name: praxis_refs fka7tyfdr3mvpml0xjobe65dq69; Type: FK CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.praxis_refs
    ADD CONSTRAINT fka7tyfdr3mvpml0xjobe65dq69 FOREIGN KEY (praxis_entity_id) REFERENCES develop.praxis(id);


--
-- Name: student_reports fkadfhbry0akgtu97d7uknnk136; Type: FK CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.student_reports
    ADD CONSTRAINT fkadfhbry0akgtu97d7uknnk136 FOREIGN KEY (student_chapter_id) REFERENCES develop.student_chapters(id);


--
-- Name: student_reports fkaf147vrllmymu1df2c5ualn8m; Type: FK CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.student_reports
    ADD CONSTRAINT fkaf147vrllmymu1df2c5ualn8m FOREIGN KEY (time_slot_id) REFERENCES develop.report_timeslots(id);


--
-- Name: levels fkc5mm3nis59ien1wj95ralvpd; Type: FK CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.levels
    ADD CONSTRAINT fkc5mm3nis59ien1wj95ralvpd FOREIGN KEY (course_id) REFERENCES develop.courses(id);


--
-- Name: profile fkccnw9lhim6xm55fqa3snj191s; Type: FK CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.profile
    ADD CONSTRAINT fkccnw9lhim6xm55fqa3snj191s FOREIGN KEY (person_id) REFERENCES develop.persons(id);


--
-- Name: chapter_parts fkdjo1wy827c44ldnk7s2ci6x6u; Type: FK CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.chapter_parts
    ADD CONSTRAINT fkdjo1wy827c44ldnk7s2ci6x6u FOREIGN KEY (chapter_id) REFERENCES develop.chapters(id);


--
-- Name: applicants fkebidt6fnr5bnbkknidci3ov57; Type: FK CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.applicants
    ADD CONSTRAINT fkebidt6fnr5bnbkknidci3ov57 FOREIGN KEY (person_id) REFERENCES develop.persons(id);


--
-- Name: praxis_refs fkee4a7h3sgelfpg93hyknbbsph; Type: FK CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.praxis_refs
    ADD CONSTRAINT fkee4a7h3sgelfpg93hyknbbsph FOREIGN KEY (refs_id) REFERENCES develop.reference_title(id);


--
-- Name: sub_chapters fkes9vj9v2pivq94kvykq8c9p5m; Type: FK CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.sub_chapters
    ADD CONSTRAINT fkes9vj9v2pivq94kvykq8c9p5m FOREIGN KEY (chapter_part_id) REFERENCES develop.chapter_parts(id);


--
-- Name: sub_chapters_skills fkfbck6gbx0anexcs6kwlphffc1; Type: FK CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.sub_chapters_skills
    ADD CONSTRAINT fkfbck6gbx0anexcs6kwlphffc1 FOREIGN KEY (sub_chapter_id) REFERENCES develop.sub_chapters(id);


--
-- Name: additionals_refs fkfbhiyjlicabi3wo3whiir38xs; Type: FK CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.additionals_refs
    ADD CONSTRAINT fkfbhiyjlicabi3wo3whiir38xs FOREIGN KEY (additional_entity_id) REFERENCES develop.additionals(id);


--
-- Name: sub_sub_chapters_refs fkg4qwiu4y733imffumdt5pvubu; Type: FK CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.sub_sub_chapters_refs
    ADD CONSTRAINT fkg4qwiu4y733imffumdt5pvubu FOREIGN KEY (sub_sub_chapter_entity_id) REFERENCES develop.sub_sub_chapters(id);


--
-- Name: sub_chapters_refs fkh2l6qo6cnm5vwxclmqgwxknis; Type: FK CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.sub_chapters_refs
    ADD CONSTRAINT fkh2l6qo6cnm5vwxclmqgwxknis FOREIGN KEY (refs_id) REFERENCES develop.reference_title(id);


--
-- Name: students_student_add_mats fkhon5u22trvj98fg2eg1vsrv5m; Type: FK CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.students_student_add_mats
    ADD CONSTRAINT fkhon5u22trvj98fg2eg1vsrv5m FOREIGN KEY (student_id) REFERENCES develop.students(id);


--
-- Name: quizes fkhqmg2ahpsjtnju6qbmhtrl7k6; Type: FK CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.quizes
    ADD CONSTRAINT fkhqmg2ahpsjtnju6qbmhtrl7k6 FOREIGN KEY (chapter_id) REFERENCES develop.chapters(id);


--
-- Name: questions fkhu7bacopenb4tq8fwpqq4mluy; Type: FK CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.questions
    ADD CONSTRAINT fkhu7bacopenb4tq8fwpqq4mluy FOREIGN KEY (quiz_id) REFERENCES develop.quizes(id);


--
-- Name: applicants fkhxyetw3xlb5e16aqmd2ymsic0; Type: FK CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.applicants
    ADD CONSTRAINT fkhxyetw3xlb5e16aqmd2ymsic0 FOREIGN KEY (student_id) REFERENCES develop.students(id);


--
-- Name: sub_chapters_skills fki84rqifacieg9gna5gk6fnahy; Type: FK CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.sub_chapters_skills
    ADD CONSTRAINT fki84rqifacieg9gna5gk6fnahy FOREIGN KEY (skill_id) REFERENCES develop.skills(id);


--
-- Name: mentors fkigjnpmup3ahkoh6rvrqqqfii4; Type: FK CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.mentors
    ADD CONSTRAINT fkigjnpmup3ahkoh6rvrqqqfii4 FOREIGN KEY (course_id) REFERENCES develop.courses(id);


--
-- Name: mentors fkigxxwva0o60cfor7aol98qmhp; Type: FK CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.mentors
    ADD CONSTRAINT fkigxxwva0o60cfor7aol98qmhp FOREIGN KEY (person_id) REFERENCES develop.persons(id);


--
-- Name: practices fkipg3o4tpeiycis36qhh1uq6vu; Type: FK CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.practices
    ADD CONSTRAINT fkipg3o4tpeiycis36qhh1uq6vu FOREIGN KEY (student_chapter_id) REFERENCES develop.student_chapters(id);


--
-- Name: reports fkj9c7op02a2h17phyw9q32op3r; Type: FK CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.reports
    ADD CONSTRAINT fkj9c7op02a2h17phyw9q32op3r FOREIGN KEY (person_id) REFERENCES develop.persons(id);


--
-- Name: graduates fkjceyrclktwnnibowfxelxat3w; Type: FK CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.graduates
    ADD CONSTRAINT fkjceyrclktwnnibowfxelxat3w FOREIGN KEY (course_id) REFERENCES develop.courses(id);


--
-- Name: additional_materials_refs fkjt6mvhgjafcwnnqdugr2hpdo5; Type: FK CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.additional_materials_refs
    ADD CONSTRAINT fkjt6mvhgjafcwnnqdugr2hpdo5 FOREIGN KEY (refs_id) REFERENCES develop.reference_title(id);


--
-- Name: reports fkkfeng1245vlaismuh0apsgvio; Type: FK CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.reports
    ADD CONSTRAINT fkkfeng1245vlaismuh0apsgvio FOREIGN KEY (course_id) REFERENCES develop.courses(id);


--
-- Name: graduates fkkv16boqox0b4hfpgeg2e8cskk; Type: FK CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.graduates
    ADD CONSTRAINT fkkv16boqox0b4hfpgeg2e8cskk FOREIGN KEY (person_id) REFERENCES develop.persons(id);


--
-- Name: statistic_student_chapters fkl5gqf3j2su734g1u66kq5kvxq; Type: FK CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.statistic_student_chapters
    ADD CONSTRAINT fkl5gqf3j2su734g1u66kq5kvxq FOREIGN KEY (course_id) REFERENCES develop.courses(id);


--
-- Name: sub_sub_chapters_refs fkle029fr3kpc9hnirlr2381mfs; Type: FK CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.sub_sub_chapters_refs
    ADD CONSTRAINT fkle029fr3kpc9hnirlr2381mfs FOREIGN KEY (refs_id) REFERENCES develop.reference_title(id);


--
-- Name: graduates fklk5ltt667ymho8bs6tj1jj1f4; Type: FK CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.graduates
    ADD CONSTRAINT fklk5ltt667ymho8bs6tj1jj1f4 FOREIGN KEY (student_id) REFERENCES develop.students(id);


--
-- Name: persons_roles fknac3je6s0rn90x5ovsqta5e9j; Type: FK CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.persons_roles
    ADD CONSTRAINT fknac3je6s0rn90x5ovsqta5e9j FOREIGN KEY (role_id) REFERENCES develop.roles(id);


--
-- Name: additionals_refs fko1x53awpydtcp28f9ynfr21cx; Type: FK CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.additionals_refs
    ADD CONSTRAINT fko1x53awpydtcp28f9ynfr21cx FOREIGN KEY (refs_id) REFERENCES develop.reference_title(id);


--
-- Name: student_chapters fko48mjj0upe6v9ddwabgp1iq6h; Type: FK CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.student_chapters
    ADD CONSTRAINT fko48mjj0upe6v9ddwabgp1iq6h FOREIGN KEY (student_id) REFERENCES develop.students(id);


--
-- Name: sub_chapters_refs fko6985e3rvth6y0vp0jttvn0il; Type: FK CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.sub_chapters_refs
    ADD CONSTRAINT fko6985e3rvth6y0vp0jttvn0il FOREIGN KEY (sub_chapter_entity_id) REFERENCES develop.sub_chapters(id);


--
-- Name: persons_roles fkpl1er8ibdrcw0milr440bvaxe; Type: FK CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.persons_roles
    ADD CONSTRAINT fkpl1er8ibdrcw0milr440bvaxe FOREIGN KEY (person_id) REFERENCES develop.persons(id);


--
-- Name: sub_sub_chapters fksacy0yd34adwea5wx3et9t6rf; Type: FK CONSTRAINT; Schema: develop; Owner: postgres
--

ALTER TABLE ONLY develop.sub_sub_chapters
    ADD CONSTRAINT fksacy0yd34adwea5wx3et9t6rf FOREIGN KEY (sub_chapter_id) REFERENCES develop.sub_chapters(id);


--
-- PostgreSQL database dump complete
--

