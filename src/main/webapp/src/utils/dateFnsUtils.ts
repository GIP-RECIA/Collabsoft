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

import { type Duration, formatDuration, intervalToDuration } from 'date-fns'

type dateType = string | number | Date

function dateToDuration(date: dateType): Duration {
  return intervalToDuration({
    start: date,
    end: Date.now(),
  })
}

function formatedDuration(duration: Duration): string {
  const { years, months, weeks, days, hours, minutes, seconds } = duration

  if (!years && !months && !weeks && !days && !hours && !minutes && !seconds)
    return formatDuration({ seconds: 1 }, { format: ['seconds'] })

  const units: { [key: string]: number | undefined } = { years, months, weeks, days, hours, minutes, seconds }

  for (const [unit, value] of Object.entries(units)) {
    if (value && value > 0) {
      return formatDuration({ [unit]: value }, { format: [unit] as Array<keyof Duration> })
    }
  }

  console.error('You\'re not supported to be here')
  return ''
}

export { dateToDuration, formatedDuration }
