import { formatDuration, intervalToDuration, parseISO } from 'date-fns';

const dateToDuration = (date: string): string => {
  const { years, months, weeks, days, hours, minutes, seconds } = intervalToDuration({
    start: parseISO(date),
    end: Date.now(),
  });

  if (
    years == undefined &&
    months == undefined &&
    weeks == undefined &&
    days == undefined &&
    hours == undefined &&
    minutes == undefined &&
    seconds == undefined
  )
    return formatDuration({ seconds: 1 }, { format: ['seconds'] });

  if (years != undefined && years > 0) return formatDuration({ years }, { format: ['years'] });
  if (months != undefined && months > 0) return formatDuration({ months }, { format: ['months'] });
  if (weeks != undefined && weeks > 0) return formatDuration({ weeks }, { format: ['weeks'] });
  if (days != undefined && days > 0) return formatDuration({ days }, { format: ['days'] });
  if (hours != undefined && hours > 0) return formatDuration({ hours }, { format: ['hours'] });
  if (minutes != undefined && minutes > 0) return formatDuration({ minutes }, { format: ['minutes'] });
  return formatDuration({ seconds }, { format: ['seconds'] });
};

export { dateToDuration };
