-- Function: public.getmaxspeed(double precision, double precision)

-- DROP FUNCTION public.getmaxspeed(double precision, double precision);

CREATE OR REPLACE FUNCTION public.getmaxspeed(
    latitude double precision,
    longitude double precision)
  RETURNS text AS
$BODY$
DECLARE
    result text;
BEGIN
	select 
		maxspeed into result from planet_osm_line
	where 
		highway is not null
                and 
                ST_DWithin(ST_SetSRID(ST_Point(latitude,longitude),4326), ST_Transform(way, 4326), 0.00013)	
	order by
                ST_Distance(ST_SetSRID(ST_Point(latitude,latitude),4326), ST_Transform(way, 4326))
	limit 1;
	RETURN result;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION public.getmaxspeed(double precision, double precision)
  OWNER TO postgis;

