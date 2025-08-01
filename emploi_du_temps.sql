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

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: classe; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.classe (
    idclasse character varying(50) NOT NULL,
    niveau character varying(50)
);


ALTER TABLE public.classe OWNER TO postgres;

--
-- Name: emploi_du_temps; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.emploi_du_temps (
    idsalle integer NOT NULL,
    idprof character varying(50) NOT NULL,
    idclasse character varying(50) NOT NULL,
    cours character varying(50),
    date timestamp(0) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE public.emploi_du_temps OWNER TO postgres;

--
-- Name: professeur; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.professeur (
    idprof character varying(50) NOT NULL,
    nom character varying(50),
    prenom character varying(50),
    grade character varying(50)
);


ALTER TABLE public.professeur OWNER TO postgres;

--
-- Name: salle; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.salle (
    idsalle integer NOT NULL,
    design character varying(50),
    occupation character varying(50)
);


ALTER TABLE public.salle OWNER TO postgres;

--
-- Data for Name: classe; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.classe (idclasse, niveau) FROM stdin;
001	L1 GB 1
002	L1 GB 2
007	L2 SR 1
003	L2 SR 2
005	L2 GB2
006	L3 SR1
008	L3 SR2
010	M1 GB
011	M2 GB
\.


--
-- Data for Name: emploi_du_temps; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.emploi_du_temps (idsalle, idprof, idclasse, cours, date) FROM stdin;
1	001	001	BDA	2025-07-21 08:00:00
1	001	001	BDR	2025-07-21 10:00:00
2	006	007	java	2025-07-21 08:00:00
2	006	007	BDA	2025-07-21 10:00:00
2	006	007	python	2025-07-21 14:00:00
2	006	007	Merise	2025-07-22 10:00:00
6	001	001	C ++	2025-07-22 10:00:00
3	001	003	C ++	2025-07-22 10:00:00
1	001	003	C ++	2025-07-08 10:00:00
1	002	003	Merise	2025-07-08 08:00:00
2	007	007	C++	2025-07-22 16:00:00
6	003	007	MTU	2025-07-24 08:00:00
6	003	007	Comptabilite	2025-07-24 10:00:00
6	003	007	Anglais	2025-07-24 16:00:00
2	008	007	Algebre	2025-07-23 10:00:00
7	002	010	BDA	2025-07-21 08:00:00
8	010	011	Java	2025-07-22 08:00:00
4	007	011	PHP	2025-07-22 14:00:00
2	003	002	BDR	2025-07-11 08:00:00
1	002	003	Merise	2025-07-28 10:00:00
1	004	003	BDR	2025-07-28 08:00:00
2	003	002	BDR	2025-07-11 14:00:00
8	001	003	Algèbre	2025-07-22 10:00:00
\.


--
-- Data for Name: professeur; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.professeur (idprof, nom, prenom, grade) FROM stdin;
001	Roget	Martinese	Professeur titulaire
002	Rindra	crisse	Assistant d’Enseignement Supérieur et de Recherche
004	Rabe		Maître Assistant
003	Radem		Assistant
006	Rabenandrasana	Philbert	Maître de Conférences
007	Luc	Hemerolde	Assistant
008	Jean		Docteur HDR
009	Randria	Lucien	Docteur HDR
010	Volasoa	Marielle	Maître de Conférences
\.


--
-- Data for Name: salle; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.salle (idsalle, design, occupation) FROM stdin;
2	Salle 002	libre
1	Salle 001	libre
3	Salle 003	libre
4	Salle 004	libre
5	Salle 005	libre
6	Salle 006	libre
7	Salle 007	libre
8	Salle 008	libre
\.


--
-- Name: classe classe_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.classe
    ADD CONSTRAINT classe_pkey PRIMARY KEY (idclasse);


--
-- Name: emploi_du_temps emploi_du_temps_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.emploi_du_temps
    ADD CONSTRAINT emploi_du_temps_pkey PRIMARY KEY (idsalle, idprof, idclasse, date);


--
-- Name: professeur professeur_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.professeur
    ADD CONSTRAINT professeur_pkey PRIMARY KEY (idprof);


--
-- Name: salle salle_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.salle
    ADD CONSTRAINT salle_pkey PRIMARY KEY (idsalle);


--
-- Name: emploi_du_temps emploi_du_temps_idclasse_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.emploi_du_temps
    ADD CONSTRAINT emploi_du_temps_idclasse_fkey FOREIGN KEY (idclasse) REFERENCES public.classe(idclasse);


--
-- Name: emploi_du_temps emploi_du_temps_idprof_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.emploi_du_temps
    ADD CONSTRAINT emploi_du_temps_idprof_fkey FOREIGN KEY (idprof) REFERENCES public.professeur(idprof);


--
-- Name: emploi_du_temps emploi_du_temps_idsalle_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.emploi_du_temps
    ADD CONSTRAINT emploi_du_temps_idsalle_fkey FOREIGN KEY (idsalle) REFERENCES public.salle(idsalle);


--
-- PostgreSQL database dump complete
--

