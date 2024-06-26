/**
 * Copyright (C) 2023 GIP-RECIA, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import { type Duration, formatDuration, intervalToDuration } from 'date-fns';

const dateToDuration = (date: string): Duration => {
  return intervalToDuration({
    start: date,
    end: Date.now(),
  });
};

const formatedDuration = (duration: Duration): string => {
  const { years, months, weeks, days, hours, minutes, seconds } = duration;

  if (!years && !months && !weeks && !days && !hours && !minutes && !seconds)
    return formatDuration({ seconds: 1 }, { format: ['seconds'] });

  if (years && years > 0) return formatDuration({ years }, { format: ['years'] });
  if (months && months > 0) return formatDuration({ months }, { format: ['months'] });
  if (weeks && weeks > 0) return formatDuration({ weeks }, { format: ['weeks'] });
  if (days && days > 0) return formatDuration({ days }, { format: ['days'] });
  if (hours && hours > 0) return formatDuration({ hours }, { format: ['hours'] });
  if (minutes && minutes > 0) return formatDuration({ minutes }, { format: ['minutes'] });
  return formatDuration({ seconds }, { format: ['seconds'] });
};

export { dateToDuration, formatedDuration };
