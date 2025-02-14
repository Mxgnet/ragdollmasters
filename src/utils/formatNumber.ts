const SYMBOLS = ['p', 'n', 'μ', 'm', '', 'k', 'M', 'G', 'T', 'P', 'E'] as const;

export function formatPhysicsValue(num: number, precision = 3): string {
  const isNegative = num < 0;
  num = Math.abs(num);
  
  if (num >= 1000) {
    let i = -1;
    while (num >= 1000) {
      num /= 1000;
      i++;
    }
    i = Math.min(i, SYMBOLS.length / 2);
    const scaled = Number((num).toPrecision(precision));
    return `${isNegative ? '-' : ''}${scaled}${SYMBOLS[i + SYMBOLS.length / 2 + 1]}`;
  } 
  
  if (num < 0.01 && num > 0) {
    let i = 0;
    while (num < 0.01) {
      num *= 1000;
      i++;
    }
    i = Math.min(i, SYMBOLS.length / 2);
    const scaled = Number((num).toPrecision(precision));
    return `${isNegative ? '-' : ''}${scaled}${SYMBOLS[SYMBOLS.length / 2 - i]}`;
  }
  
  return `${isNegative ? '-' : ''}${num.toPrecision(precision)}`;
} 