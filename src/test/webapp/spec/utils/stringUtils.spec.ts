// @ts-ignore
import { charOTP, interpolate, slugify } from '@/utils/stringUtils';
import { describe, expect, it } from 'vitest';

describe('stringUtils', () => {
  it('charOTP', () => {
    expect(charOTP()).length(6);
    expect(charOTP(8)).length(8);
    expect(charOTP()).not.toBe(charOTP());
  });

  it('slugify', () => {
    expect(slugify('Foo Bar --Bar été ')).toBe('foo-bar-bar-ete');
  });

  it('interpolate', () => {
    expect(interpolate('foo ${bar}', { bar: 'bar !' })).toBe('foo bar !');
  });
});
