export function formatPhysicsValue(num: number, precision: number = 3): string {
  const units = ['p', 'n', 'μ', 'm', '', 'k', 'M', 'G', 'T'];
  const unitIndex = Math.floor(Math.log10(Math.abs(num)) / 3) + 4;
  
  if (unitIndex < 0 || unitIndex >= units.length) {
    return num.toPrecision(precision);
  }

  const scaledNum = num / Math.pow(10, (unitIndex - 4) * 3);
  return `${scaledNum.toPrecision(precision)}${units[unitIndex]}`;
} 