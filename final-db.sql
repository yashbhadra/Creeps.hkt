--
-- PostgreSQL database dump
--

-- Dumped from database version 9.6.3
-- Dumped by pg_dump version 9.6.3

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: tiger; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA tiger;


ALTER SCHEMA tiger OWNER TO postgres;

--
-- Name: tiger_data; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA tiger_data;


ALTER SCHEMA tiger_data OWNER TO postgres;

--
-- Name: topology; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA topology;


ALTER SCHEMA topology OWNER TO postgres;

--
-- Name: SCHEMA topology; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON SCHEMA topology IS 'PostGIS Topology schema';


--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


--
-- Name: address_standardizer; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS address_standardizer WITH SCHEMA public;


--
-- Name: EXTENSION address_standardizer; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION address_standardizer IS 'Used to parse an address into constituent elements. Generally used to support geocoding address normalization step.';


--
-- Name: address_standardizer_data_us; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS address_standardizer_data_us WITH SCHEMA public;


--
-- Name: EXTENSION address_standardizer_data_us; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION address_standardizer_data_us IS 'Address Standardizer US dataset example';


--
-- Name: fuzzystrmatch; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS fuzzystrmatch WITH SCHEMA public;


--
-- Name: EXTENSION fuzzystrmatch; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION fuzzystrmatch IS 'determine similarities and distance between strings';


--
-- Name: ogr_fdw; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS ogr_fdw WITH SCHEMA public;


--
-- Name: EXTENSION ogr_fdw; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION ogr_fdw IS 'foreign-data wrapper for GIS data access';


--
-- Name: postgis; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS postgis WITH SCHEMA public;


--
-- Name: EXTENSION postgis; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION postgis IS 'PostGIS geometry, geography, and raster spatial types and functions';


--
-- Name: pgrouting; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS pgrouting WITH SCHEMA public;


--
-- Name: EXTENSION pgrouting; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION pgrouting IS 'pgRouting Extension';


--
-- Name: pointcloud; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS pointcloud WITH SCHEMA public;


--
-- Name: EXTENSION pointcloud; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION pointcloud IS 'data type for lidar point clouds';


--
-- Name: pointcloud_postgis; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS pointcloud_postgis WITH SCHEMA public;


--
-- Name: EXTENSION pointcloud_postgis; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION pointcloud_postgis IS 'integration for pointcloud LIDAR data and PostGIS geometry data';


--
-- Name: postgis_sfcgal; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS postgis_sfcgal WITH SCHEMA public;


--
-- Name: EXTENSION postgis_sfcgal; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION postgis_sfcgal IS 'PostGIS SFCGAL functions';


--
-- Name: postgis_tiger_geocoder; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS postgis_tiger_geocoder WITH SCHEMA tiger;


--
-- Name: EXTENSION postgis_tiger_geocoder; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION postgis_tiger_geocoder IS 'PostGIS tiger geocoder and reverse geocoder';


--
-- Name: postgis_topology; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS postgis_topology WITH SCHEMA topology;


--
-- Name: EXTENSION postgis_topology; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION postgis_topology IS 'PostGIS topology spatial types and functions';


SET search_path = public, pg_catalog;

--
-- Name: show_cities(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION show_cities() RETURNS TABLE(city_name character varying, distance double precision)
    LANGUAGE plpgsql
    AS $$
BEGIN
   RETURN QUERY
   SELECT name ,
    ST_Distance(
			ST_Transform((SELECT location FROM city WHERE city_id =1)::geometry,26986),
			ST_Transform((location)::geometry,26986)
		) as distance 
FROM
    city where ST_Distance(
			ST_Transform((SELECT location FROM city WHERE city_id =1)::geometry,26986),
			ST_Transform((location)::geometry,26986)
		) <= 300*100 order by distance ;
END
$$;


ALTER FUNCTION public.show_cities() OWNER TO postgres;

--
-- Name: show_cities(double precision, double precision, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION show_cities(lon double precision, lat double precision, dist double precision) RETURNS TABLE(city_name character varying, distance double precision)
    LANGUAGE plpgsql
    AS $$
BEGIN
   RETURN QUERY
   SELECT name ,
    ST_Distance(
			ST_Transform((ST_Point(lon,lat))::geometry,26986),
			ST_Transform((location)::geometry,26986)
		) as distance 
FROM
    city where ST_Distance(
			ST_Transform((SELECT location FROM city WHERE city_id =1)::geometry,26986),
			ST_Transform((location)::geometry,26986)
		) <= dist*100 order by distance ;
END
$$;


ALTER FUNCTION public.show_cities(lon double precision, lat double precision, dist double precision) OWNER TO postgres;

--
-- Name: word_frequency(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION word_frequency(_max_tokens integer) RETURNS TABLE(city_name character varying, center character varying, distance bigint)
    LANGUAGE plpgsql
    AS $$
BEGIN
   RETURN QUERY
   SELECT name , ' mumbai ',
    ST_Distance(
			ST_Transform((SELECT location FROM city WHERE city_id =1)::geometry,26986),
			ST_Transform((location)::geometry,26986)
		) as distance 
FROM
    city where ST_Distance(
			ST_Transform((SELECT location FROM city WHERE city_id =1)::geometry,26986),
			ST_Transform((location)::geometry,26986)
		) <= 300*100 order by distance ;
END
$$;


ALTER FUNCTION public.word_frequency(_max_tokens integer) OWNER TO postgres;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: city; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE city (
    city_id integer NOT NULL,
    name character varying,
    state_id integer,
    location geometry(Point,4326)
);


ALTER TABLE city OWNER TO postgres;

--
-- Name: country; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE country (
    gid integer NOT NULL,
    area double precision,
    perimeter double precision,
    cntry_ numeric,
    cntry_id numeric,
    fips_cntry character varying,
    gmi_cntry character varying,
    cntry_name character varying,
    sovereign character varying,
    pop_cntry double precision,
    curr_type character varying,
    curr_code character varying,
    landlocked character varying,
    color_map character varying,
    sovereign_ character varying,
    the_geom geometry(MultiPolygon)
);


ALTER TABLE country OWNER TO postgres;

--
-- Name: country_gid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE country_gid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE country_gid_seq OWNER TO postgres;

--
-- Name: country_gid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE country_gid_seq OWNED BY country.gid;


--
-- Name: domain; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE domain (
    domain_id integer NOT NULL,
    locale_id integer,
    item_id integer
);


ALTER TABLE domain OWNER TO postgres;

--
-- Name: item; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE item (
    item_id integer NOT NULL,
    message text
);


ALTER TABLE item OWNER TO postgres;

--
-- Name: locale; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE locale (
    locale_id integer NOT NULL,
    name character varying,
    location geometry(Point),
    city_id integer
);


ALTER TABLE locale OWNER TO postgres;

--
-- Name: locale_locale_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE locale_locale_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE locale_locale_id_seq OWNER TO postgres;

--
-- Name: locale_locale_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE locale_locale_id_seq OWNED BY locale.locale_id;


--
-- Name: river; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE river (
    gid integer,
    name character varying,
    diak_cde character varying,
    name2 character varying,
    diak_cde2 character varying,
    type character varying,
    status character varying,
    navig character varying,
    the_geom geometry(MultiLineString)
);


ALTER TABLE river OWNER TO postgres;

--
-- Name: state; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE state (
    state_id integer NOT NULL,
    name character varying,
    location geometry(Point,4326)
);


ALTER TABLE state OWNER TO postgres;

--
-- Name: state_state_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE state_state_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE state_state_id_seq OWNER TO postgres;

--
-- Name: state_state_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE state_state_id_seq OWNED BY city.city_id;


--
-- Name: state_state_id_seq1; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE state_state_id_seq1
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE state_state_id_seq1 OWNER TO postgres;

--
-- Name: state_state_id_seq1; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE state_state_id_seq1 OWNED BY state.state_id;


--
-- Name: subscription; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE subscription (
    subscription_id integer NOT NULL,
    locale_id bigint,
    username character varying
);


ALTER TABLE subscription OWNER TO postgres;

--
-- Name: subscription_subscription_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE subscription_subscription_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE subscription_subscription_id_seq OWNER TO postgres;

--
-- Name: subscription_subscription_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE subscription_subscription_id_seq OWNED BY subscription.subscription_id;


--
-- Name: city city_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY city ALTER COLUMN city_id SET DEFAULT nextval('state_state_id_seq'::regclass);


--
-- Name: country gid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY country ALTER COLUMN gid SET DEFAULT nextval('country_gid_seq'::regclass);


--
-- Name: locale locale_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY locale ALTER COLUMN locale_id SET DEFAULT nextval('locale_locale_id_seq'::regclass);


--
-- Name: state state_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY state ALTER COLUMN state_id SET DEFAULT nextval('state_state_id_seq1'::regclass);


--
-- Name: subscription subscription_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY subscription ALTER COLUMN subscription_id SET DEFAULT nextval('subscription_subscription_id_seq'::regclass);


--
-- Data for Name: city; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY city (city_id, name, state_id, location) FROM stdin;
1	Mumbai	1	0101000020E61000000DE9A6832B3852407187F2AA73133340
2	Pune	1	0101000020E6100000752387E3D4765240F4588EEB3A853240
4	Kolhapur	1	0101000020E61000007ACAC573918F52406898350C7AB43040
3	 Nagpur	1	0101000020E6100000DCCE6339AEC5534059E02BBAF5263540
5	Thane	1	0101000020E61000007C838A05993E524079365085E4373340
6	Amravati	1	0101000020E610000079C3222BE4715340F77D9301FBEF3440
\.


--
-- Data for Name: country; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY country (gid, area, perimeter, cntry_, cntry_id, fips_cntry, gmi_cntry, cntry_name, sovereign, pop_cntry, curr_type, curr_code, landlocked, color_map, sovereign_, the_geom) FROM stdin;
0	1116.6700000000001	726.10000000000002	66.000000000	66.000000000	US	USA	United States	United States	258833000	Dollar	USD	N	5	United States	0106000000010000000103000000010000008E00000091D61874424059C0BA490C022B1F434091D61874424059C0F58590F3FE7F484043C879FF1F2059C07C9C69C2F67F4840ACC8E880240059C00000000000804840807EDFBF79F558C00000000000804840807EDFBF79D558C027BD6F7CED7F4840E884D04197B758C00000000000804840ACC8E88024A058C027BD6F7CED7F4840950ED6FF399158C0D200DE0209804840A9A10DC0067E58C0D200DE020980484094145800537358C00000000000804840D5EB1681B16858C027BD6F7CED7F4840302C7FBE2D6058C027BD6F7CED7F484043C5387F134E58C07C9C69C2F67F48405DA3E5400FD157C048E00F3FFF7F484017B7D100DEC957C027BD6F7CED7F484001F73C7FDAC957C0D6AC33BE2F96484098F90E7EE2C957C0A96BED7DAAAA484017B7D100DEC957C02BDB87BCE5AE48405952EE3EC7C957C0CCEB884336B048405567B5C01EC957C0FCA5457D92AF4840AC8F87BEBBC757C07E8D2441B8AE484028F04E3E3DC557C09DF3531C07AE4840168A743FA7C157C05531957EC2AD4840419B1C3EE9BF57C05531957EC2AD4840C173EFE192BD57C0276BD44334AE48407009C03FA5BB57C0672783A3E4AD4840AAD4EC8156B457C039B69E211CA74840FEF3346090B357C02638F581E49748406BD619DF17B357C039B9DFA1289448406C2409C215B157C07E8AE3C0AB894840282CF180B2AF57C0A3C85A43A98348407E8D2441B8AE57C051853FC39B7F48407E6FD39FFDAD57C0F50EB743C37C484043CBBA7F2CAD57C0E38A8BA372774840838769DFDCAC57C039EFFFE384734840C77F812040AD57C04704E3E0D26D48405473B9C150AD57C0253E7782FD654840D3BEB9BF7AAC57C0240B98C0AD6348402B2FF99FFCA857C0A777F17EDC5E48406BF12900C6A657C0240ED940BA5C48406DE2E47E87A157C079758E01D9594840ED6305BF0DA057C07AABAE43355948401781B1BE819D57C0658EE55DF5584840116F9D7FBB9B57C079758E01D9594840D8B8FE5D9F9957C0910E0F61FC5A48409335EA211A9357C0FDA204FD855A484015C616821C9157C026C632FD12594840A1866F61DD8F57C0E6E8F17B9B5448409544F641968857C0FAF02C41465248407F4B00FE298757C07BA2EBC20F524840BF64E3C1168457C0FCABC77DAB5148401781B1BE817857C0D1CABDC0AC504840419E5DBEF57657C0118DEE2076504840941799805F7557C0BA675DA3E54E4840187B2FBE687457C02541B8020A4B4840BE4F55A1817357C0BAF59A1E144448405743E21E4B7257C094BDA59C2F424840EA758BC0586E57C0D673D2FBC641484016A243E0486A57C0D3FA5B02F0414840962023A0C25F57C0672AC423F1444840971DE21FB65D57C0FCA886FD9E464840B7D4415E0F5D57C0CE001764CB4A48408369183E225A57C07E1B62BCE64D4840809C3061345457C02502D53F88504840BF2B82FFAD4F57C0253E7782FD5148403FC8B260E23C57C0A54DD53DB24F48405952EE3EC72D57C0505260014C45484068CA4E3FA82C57C0CDE506431D3E484001309E41432557C0A19E3E027F384840D6A9F23D231D57C0A73E90BC73324840ABD1AB014A1B57C02638F581E42748403DD175E1071757C0A3C85A43A91D4840302FC03E3A1557C0D47FD6FCF81D484004392861A61257C09C1727BEDA1F4840BF4351A04F1257C0109370218F224840D6AC33BE2F1357C065C405A0512648402B4D4A41B71157C028BA2EFCE02A4840ED9C6681761057C07D5A457F682C4840D4B837BF610A57C07B698A00A72D484003249A40110957C0D1CDFE40B92D4840EC51B81E850257C0E57B4622342C48409529E620E80057C0CFDC43C2F72648409911DE1E840057C03AADDBA0F62348403F8F519E79FF56C0FC1A49827021484089EAAD81ADFD56C05DC136E2C91E4840950B957F2DFC56C0CFA0A17F821D4840C11C3D7E6FF656C07C2766BD181A4840556AF6402BF256C0221ADD41EC184840FFE7305F5EEF56C0A19E3E027F1848400000000000EC56C0FDA204FD85124840ABD1AB014AE956C03065E080960C4840D80C7041B6E456C0A77A32FFE80B48402C47C8409EDD56C04F58E201650748406BF12900C6DA56C07E5182FE42054840A9BF5E61C1D856C0BA313D618907484042CEFBFF38D656C0552E54FEB50848405649641F64D456C092088D60E308484041BCAE5FB0D156C0505260014C094840EA78CC4065CF56C07A6CCB80B30A4840A7B393C151CC56C07E8D2441B80E4840D6E253008CC956C0A73E90BC73124840BB438A0112C856C05116BEBED6134840A9A10DC006BE56C026FF93BF7B1B48406BBB09BE69BB56C0CCEEC9C3421D4840807EDFBF79B956C07B336ABE4A1E4840E884D04197B756C055FB743C661E48406B0C3A2174B556C0CEE2C5C2101D4840CC7F48BF7DB556C08AABCABE2B1A48406DE2E47E87B156C041834D9D470D4840C05E61C1FDAF56C064062AE3DF0B48406AFAEC80EB9156C0AB5FE97C780E4840284701A2608956C0E2900DA48B0F4840FE0B0401328456C0D2C77C40A00D4840FF058200198256C0D47C957CEC0848400118CFA0A17F56C0234DBC033C03484054556820967956C07A724D81CCFE4740EC6CC83F337756C0286552431B00484082AB3C81B07556C001C11C3D7E0148406C06B8205B7056C0BDE3141DC90348405534D6FECE6656C039EFFFE384014840EA5A7B9FAA6456C010B1C1C249004840ABB35A608F5F56C0C53A55BE6700484068CA4E3FA85C56C0FE9C82FC6C004840BD344580D35656C0A73B4F3C67FD4740D5EB1681B15456C07AA52C431CFF4740EA758BC0583E56C0A774B0FECF11484054707841442C56C0552E54FEB52048405473B9C1502956C0A774B0FECF2148401684F23E8E1756C0F9BD4D7FF627484052431B800D0C56C0A38FF980401F48406ABE4A3E76DC55C05055A18158FA474017B7D100DED555C0768A558330F547406E1805C1E3CC55C08048BF7D1DEE4740168733BF9AB855C02AE44A3D0BDE4740187B2FBE68A455C07D96E7C1DDCD47407D96E7C1DD9D55C07B6649809AC84740ABD1AB014A8355C0A583F57F0EB34740C05B2041F18055C024473A0323B1474088BEBB95257A55C0685C381092AB474088BEBB95257A55C0BA490C022B1F434091D61874424059C0BA490C022B1F4340
1	1694.0219999999999	2210.5799999999999	252.000000000	252.000000000	CA	CAN	Canada	Canada	28402320	Dollar	CAD	N	4	Canada	0106000000010000000103000000010000008E00000091D61874424059C0F58590F3FE7F484091D61874424059C0DC4603780BCC494088BEBB95257A55C0DC4603780BCC494088BEBB95257A55C0685C381092AB4740C05B2041F18055C024473A0323B14740ABD1AB014A8355C0A583F57F0EB347407D96E7C1DD9D55C07B6649809AC84740187B2FBE68A455C07D96E7C1DDCD4740168733BF9AB855C02AE44A3D0BDE47406E1805C1E3CC55C08048BF7D1DEE474017B7D100DED555C0768A558330F547406ABE4A3E76DC55C05055A18158FA474052431B800D0C56C0A38FF980401F48401684F23E8E1756C0F9BD4D7FF62748405473B9C1502956C0A774B0FECF21484054707841442C56C0552E54FEB5204840EA758BC0583E56C0A774B0FECF114840D5EB1681B15456C07AA52C431CFF4740BD344580D35656C0A73B4F3C67FD474068CA4E3FA85C56C0FE9C82FC6C004840ABB35A608F5F56C0C53A55BE67004840EA5A7B9FAA6456C010B1C1C2490048405534D6FECE6656C039EFFFE3840148406C06B8205B7056C0BDE3141DC903484082AB3C81B07556C001C11C3D7E014840EC6CC83F337756C0286552431B00484054556820967956C07A724D81CCFE47400118CFA0A17F56C0234DBC033C034840FF058200198256C0D47C957CEC084840FE0B0401328456C0D2C77C40A00D4840284701A2608956C0E2900DA48B0F48406AFAEC80EB9156C0AB5FE97C780E4840C05E61C1FDAF56C064062AE3DF0B48406DE2E47E87B156C041834D9D470D4840CC7F48BF7DB556C08AABCABE2B1A48406B0C3A2174B556C0CEE2C5C2101D4840E884D04197B756C055FB743C661E4840807EDFBF79B956C07B336ABE4A1E48406BBB09BE69BB56C0CCEEC9C3421D4840A9A10DC006BE56C026FF93BF7B1B4840BB438A0112C856C05116BEBED6134840D6E253008CC956C0A73E90BC73124840A7B393C151CC56C07E8D2441B80E4840EA78CC4065CF56C07A6CCB80B30A484041BCAE5FB0D156C0505260014C0948405649641F64D456C092088D60E308484042CEFBFF38D656C0552E54FEB5084840A9BF5E61C1D856C0BA313D61890748406BF12900C6DA56C07E5182FE420548402C47C8409EDD56C04F58E20165074840D80C7041B6E456C0A77A32FFE80B4840ABD1AB014AE956C03065E080960C48400000000000EC56C0FDA204FD85124840FFE7305F5EEF56C0A19E3E027F184840556AF6402BF256C0221ADD41EC184840C11C3D7E6FF656C07C2766BD181A4840950B957F2DFC56C0CFA0A17F821D484089EAAD81ADFD56C05DC136E2C91E48403F8F519E79FF56C0FC1A4982702148409911DE1E840057C03AADDBA0F62348409529E620E80057C0CFDC43C2F7264840EC51B81E850257C0E57B4622342C484003249A40110957C0D1CDFE40B92D4840D4B837BF610A57C07B698A00A72D4840ED9C6681761057C07D5A457F682C48402B4D4A41B71157C028BA2EFCE02A4840D6AC33BE2F1357C065C405A051264840BF4351A04F1257C0109370218F22484004392861A61257C09C1727BEDA1F4840302FC03E3A1557C0D47FD6FCF81D48403DD175E1071757C0A3C85A43A91D4840ABD1AB014A1B57C02638F581E4274840D6A9F23D231D57C0A73E90BC7332484001309E41432557C0A19E3E027F38484068CA4E3FA82C57C0CDE506431D3E48405952EE3EC72D57C0505260014C4548403FC8B260E23C57C0A54DD53DB24F4840BF2B82FFAD4F57C0253E7782FD514840809C3061345457C02502D53F885048408369183E225A57C07E1B62BCE64D4840B7D4415E0F5D57C0CE001764CB4A4840971DE21FB65D57C0FCA886FD9E464840962023A0C25F57C0672AC423F144484016A243E0486A57C0D3FA5B02F0414840EA758BC0586E57C0D673D2FBC64148405743E21E4B7257C094BDA59C2F424840BE4F55A1817357C0BAF59A1E14444840187B2FBE687457C02541B8020A4B4840941799805F7557C0BA675DA3E54E4840419E5DBEF57657C0118DEE20765048401781B1BE817857C0D1CABDC0AC504840BF64E3C1168457C0FCABC77DAB5148407F4B00FE298757C07BA2EBC20F5248409544F641968857C0FAF02C4146524840A1866F61DD8F57C0E6E8F17B9B54484015C616821C9157C026C632FD125948409335EA211A9357C0FDA204FD855A4840D8B8FE5D9F9957C0910E0F61FC5A4840116F9D7FBB9B57C079758E01D95948401781B1BE819D57C0658EE55DF5584840ED6305BF0DA057C07AABAE43355948406DE2E47E87A157C079758E01D95948406BF12900C6A657C0240ED940BA5C48402B2FF99FFCA857C0A777F17EDC5E4840D3BEB9BF7AAC57C0240B98C0AD6348405473B9C150AD57C0253E7782FD654840C77F812040AD57C04704E3E0D26D4840838769DFDCAC57C039EFFFE38473484043CBBA7F2CAD57C0E38A8BA3727748407E6FD39FFDAD57C0F50EB743C37C48407E8D2441B8AE57C051853FC39B7F4840282CF180B2AF57C0A3C85A43A98348406C2409C215B157C07E8AE3C0AB8948406BD619DF17B357C039B9DFA128944840FEF3346090B357C02638F581E4974840AAD4EC8156B457C039B69E211CA748407009C03FA5BB57C0672783A3E4AD4840C173EFE192BD57C0276BD44334AE4840419B1C3EE9BF57C05531957EC2AD4840168A743FA7C157C05531957EC2AD484028F04E3E3DC557C09DF3531C07AE4840AC8F87BEBBC757C07E8D2441B8AE48405567B5C01EC957C0FCA5457D92AF48405952EE3EC7C957C0CCEB884336B0484017B7D100DEC957C02BDB87BCE5AE484098F90E7EE2C957C0A96BED7DAAAA484001F73C7FDAC957C0D6AC33BE2F96484017B7D100DEC957C027BD6F7CED7F48405DA3E5400FD157C048E00F3FFF7F484043C5387F134E58C07C9C69C2F67F4840302C7FBE2D6058C027BD6F7CED7F4840D5EB1681B16858C027BD6F7CED7F484094145800537358C00000000000804840A9A10DC0067E58C0D200DE0209804840950ED6FF399158C0D200DE0209804840ACC8E88024A058C027BD6F7CED7F4840E884D04197B758C00000000000804840807EDFBF79D558C027BD6F7CED7F4840807EDFBF79F558C00000000000804840ACC8E880240059C0000000000080484043C879FF1F2059C07C9C69C2F67F484091D61874424059C0F58590F3FE7F4840
\.


--
-- Name: country_gid_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('country_gid_seq', 1, true);


--
-- Data for Name: domain; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY domain (domain_id, locale_id, item_id) FROM stdin;
1	1	1
\.


--
-- Data for Name: item; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY item (item_id, message) FROM stdin;
1	{"title":"Times of India","description":"An Indian warship, the stealth frigate INS Trishul, thwarted a piracy attempt on a merchant vessel Jag Amar in a swift operation by deploying an armed helicopter and marine commandos in the Gulf of Aden on Friday.","link":"https://timesofindia.indiatimes.com/india/navy-prevents-pirate-attack-on-indian-ship-in-gulf-of-aden/articleshow/60972567.cms","author":"","guid":"https://timesofindia.indiatimes.com/india/navy-prevents-pirate-attack-on-indian-ship-in-gulf-of-aden/articleshow/60972567.cms"}
2	{"title":"Live: Heavy showers, thunderstorms lash Mumbai","description":"","link":"http://timesofindia.indiatimes.com/city/mumbai/rains-lash-mumbai/liveblog/60973462.cms","author":"","guid":"http://timesofindia.indiatimes.com/city/mumbai/rains-lash-mumbai/liveblog/60973462.cms"}
\.


--
-- Data for Name: locale; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY locale (locale_id, name, location, city_id) FROM stdin;
1	Airoli	0101000020E610000004159F4CED3F52404424F90093283340	1
2	Ambernath	0101000020E610000026CD7A8CCD4B52404B3CA06CCA353340	1
5	Asangaon	0101000020E6100000322A5F86B55352405BD3179D87703340	1
8	Baman Dongari	0101000020E61000004527F0F384415240DE150B8D71F93240	1
9	Bandra	0101000020E610000038AE354ACD355240141049E3060E3340	1
10	Bhandup	0101000020E61000002ED1B41F043C5240A2597E8571243340	1
11	Bhivpuri Road	0101000020E6100000FB97FFEB375552407C48539852F83240	1
13	Byculla	0101000020E6100000DADF7E654A355240CFB7AA9102FA3240	1
14	Mumbai Chhatrapati Shivaji Maharaj Terminus	0101000020E610000087B7184D78355240BE19901898F03240	1
15	Chinchpokli	0101000020E6100000808E45894C355240CBD0C2AFA2FC3240	1
16	Currey Road	0101000020E6100000D2B716774D3552406D3590D37CFE3240	1
17	Dadar	0101000020E610000025B1A4DCFD355240C767B27F9E043340	1
18	Dahanu Road	0101000020E610000042A55BC0952F52409F8B2BD3D4FD3340	1
20	Mulund	0101000020E61000008F311C74AE3D5240B0F72C1911283340	1
19	Vikhroli	0101000020E61000000C7151D2683B5240D02C6409C61C3340	1
21	Nahur	0101000020E610000024A3B904963C5240598231C797273340	1
22	Kanjurmarg	0101000020E6100000FCE3BD6A653B52403BB885F8D1203340	1
23	Vikhroli	0101000020E61000000C7151D2683B5240D02C6409C61C3340	1
24	Ghatkopar	0101000020E61000003703B749203A52408D6E7319ED153340	1
25	Vidyavihar	0101000020E610000013CA0C6572395240E3DAF5775B143340	1
26	Kurla	0101000020E610000000975BB546385240D51AEF33D3103340	1
27	Sion	0101000020E6100000AAEE91CD553752409C8C2AC3B80B3340	1
28	Matunga	0101000020E6100000285D9FDE31365240065D67E844073340	1
29	Sandhurst Road	0101000020E610000019AE0E80B8355240B262B83A00F63240	1
30	Masjib Bunder	0101000020E6100000B10CBB39A63552400807D6CCB5F33240	1
31	Parel	0101000020E61000007065E48725355240EC6580B0F8FE3240	1
32	Thane	0101000020E6100000031203136E3E5240FF8DE2C1BB2F3340	5
33	Kalwa	0101000020E610000051E56A1ACA3F5240D0D7875000323340	5
34	Mumbra	0101000020E6100000F7AE415F7A415240E5B0A0D56E303340	5
35	Diva	0101000020E61000000EBB945FAB4252408514F2F741303340	5
36	Dombivli	0101000020E6100000FC6F253BB6455240B10408D5BC373340	5
37	Thakurli	0101000020E61000005CD71EAC35465240FA916CBFC6393340	5
38	Kalyan	0101000020E61000006EBF7CB262485240D13538B6433C3340	5
39	Titwala	0101000020E6100000BEE4DAABEA4C52403050AD19744B3340	5
40	Ambivalli	0101000020E6100000B0FECF61BE41524034B91803EBB83340	5
41	Khadavli	0101000020E61000006B7EFCA5454E5240888384285F583340	5
42	Vasind	0101000020E610000031FEC75320515240EE1692DD16683340	5
43	Kardi	0101000020E6100000D204E511375952403C832B4597943340	5
44	Kasara	0101000020E6100000887A66EE465E52407A90F9DBF9A53340	5
12	Bhiwandi	0101000020E6100000F9331713F64252406BF46A80D2443340	5
3	Ambivli	0101000020E6100000966C8665FD4A52408DC8C1B68F443340	2
4	Andheri	0101000020E61000009CB7FB662C365240C444DECDAE1E3340	2
6	Atgaon	0101000020E6100000DB961293015552402142B7F2ED803340	2
7	Badlapur	0101000020E61000001F9DBAF2594F52400F99F221A82A3340	2
\.


--
-- Name: locale_locale_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('locale_locale_id_seq', 1, false);


--
-- Data for Name: pointcloud_formats; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY pointcloud_formats (pcid, srid, schema) FROM stdin;
\.


--
-- Data for Name: river; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY river (gid, name, diak_cde, name2, diak_cde2, type, status, navig, the_geom) FROM stdin;
\.


--
-- Data for Name: spatial_ref_sys; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY spatial_ref_sys (srid, auth_name, auth_srid, srtext, proj4text) FROM stdin;
\.


--
-- Data for Name: state; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY state (state_id, name, location) FROM stdin;
1	Mumbai	0101000020E61000009ABFF858B0ED5240C89AECFA60C03340
\.


--
-- Name: state_state_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('state_state_id_seq', 9, true);


--
-- Name: state_state_id_seq1; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('state_state_id_seq1', 1, true);


--
-- Data for Name: subscription; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY subscription (subscription_id, locale_id, username) FROM stdin;
1	1	yash
\.


--
-- Name: subscription_subscription_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('subscription_subscription_id_seq', 1, false);


--
-- Data for Name: us_gaz; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY us_gaz (id, seq, word, stdword, token, is_custom) FROM stdin;
\.


--
-- Data for Name: us_lex; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY us_lex (id, seq, word, stdword, token, is_custom) FROM stdin;
\.


--
-- Data for Name: us_rules; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY us_rules (id, rule, is_custom) FROM stdin;
\.


SET search_path = tiger, pg_catalog;

--
-- Data for Name: geocode_settings; Type: TABLE DATA; Schema: tiger; Owner: postgres
--

COPY geocode_settings (name, setting, unit, category, short_desc) FROM stdin;
\.


--
-- Data for Name: pagc_gaz; Type: TABLE DATA; Schema: tiger; Owner: postgres
--

COPY pagc_gaz (id, seq, word, stdword, token, is_custom) FROM stdin;
\.


--
-- Data for Name: pagc_lex; Type: TABLE DATA; Schema: tiger; Owner: postgres
--

COPY pagc_lex (id, seq, word, stdword, token, is_custom) FROM stdin;
\.


--
-- Data for Name: pagc_rules; Type: TABLE DATA; Schema: tiger; Owner: postgres
--

COPY pagc_rules (id, rule, is_custom) FROM stdin;
\.


SET search_path = topology, pg_catalog;

--
-- Data for Name: topology; Type: TABLE DATA; Schema: topology; Owner: postgres
--

COPY topology (id, name, srid, "precision", hasz) FROM stdin;
\.


--
-- Data for Name: layer; Type: TABLE DATA; Schema: topology; Owner: postgres
--

COPY layer (topology_id, layer_id, schema_name, table_name, feature_column, feature_type, level, child_id) FROM stdin;
\.


SET search_path = public, pg_catalog;

--
-- Name: country country_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY country
    ADD CONSTRAINT country_pkey PRIMARY KEY (gid);


--
-- Name: domain domain_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY domain
    ADD CONSTRAINT domain_pkey PRIMARY KEY (domain_id);


--
-- Name: item item_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY item
    ADD CONSTRAINT item_pkey PRIMARY KEY (item_id);


--
-- Name: locale locale_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY locale
    ADD CONSTRAINT locale_pkey PRIMARY KEY (locale_id);


--
-- Name: city state_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY city
    ADD CONSTRAINT state_pkey PRIMARY KEY (city_id);


--
-- Name: state state_pkey1; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY state
    ADD CONSTRAINT state_pkey1 PRIMARY KEY (state_id);


--
-- Name: subscription subscription_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY subscription
    ADD CONSTRAINT subscription_pkey PRIMARY KEY (subscription_id);


--
-- Name: city city_state_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY city
    ADD CONSTRAINT city_state_id_fkey FOREIGN KEY (state_id) REFERENCES state(state_id);


--
-- Name: domain domain_item_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY domain
    ADD CONSTRAINT domain_item_id_fkey FOREIGN KEY (item_id) REFERENCES item(item_id);


--
-- Name: domain domain_locale_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY domain
    ADD CONSTRAINT domain_locale_id_fkey FOREIGN KEY (locale_id) REFERENCES locale(locale_id);


--
-- Name: locale locale_city_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY locale
    ADD CONSTRAINT locale_city_id_fkey FOREIGN KEY (city_id) REFERENCES city(city_id);


--
-- Name: subscription subscription_locale_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY subscription
    ADD CONSTRAINT subscription_locale_id_fkey FOREIGN KEY (locale_id) REFERENCES locale(locale_id);


--
-- PostgreSQL database dump complete
--

