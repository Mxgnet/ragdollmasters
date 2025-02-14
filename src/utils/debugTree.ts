interface TreeNode {
  id: string | number;
  label?: string;
  children?: TreeNode[];
}

export function createDebugTree(composite: Matter.Composite): TreeNode {
  return {
    id: composite.id,
    label: composite.label,
    children: [
      ...composite.bodies.map(body => ({
        id: body.id,
        label: body.label,
        children: []
      })),
      ...composite.constraints.map(constraint => ({
        id: constraint.id,
        label: 'Constraint',
        children: []
      })),
      ...composite.composites.map(child => createDebugTree(child))
    ]
  };
}

export function printDebugTree(node: TreeNode, level = 0): string {
  const indent = '  '.repeat(level);
  const prefix = level === 0 ? '' : level === 1 ? '├─ ' : '│  '.repeat(level - 1) + '├─ ';
  
  let output = `${indent}${prefix}${node.label || node.id}\n`;
  
  if (node.children) {
    node.children.forEach((child, i) => {
      output += printDebugTree(child, level + 1);
    });
  }
  
  return output;
} 