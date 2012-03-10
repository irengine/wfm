CREATE FUNCTION `getVacationDays`(`joinAt` date,`calculateAt` date)
 RETURNS int(11)
BEGIN
DECLARE	years INT;
DECLARE	months INT;
DECLARE days INT;

	SET years = (YEAR(calculateAt)-YEAR(joinAt)) - (RIGHT(calculateAt,5)<RIGHT(joinAt,5));
	SET months = MONTH(calculateAt);
	SET days = 0;

		if (years = 0) then
			set days = 0;
		elseif (years = 1) then
			set days = (5 * (12 - month + 1)) / 12;
		elseif (years > 1 && years < 10) then
			set days = 5;
		elseif (years = 10) then
			set days = 5 * (month - 1) / 12 + 10 * (12 - month + 1) /12 ;
		elseif (years > 10 && years < 20) then
			set days = 10;
		elseif (years = 20) then
			set days = 10 * (month - 1) / 12 + 15 * (12 - month + 1) /12 ;
		elseif (years > 20) then
			set days = 15;
		end if;

	RETURN days;
END;
