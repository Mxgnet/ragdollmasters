/*
 * Decompiled with CFR 0.152.
 */
package Nodes;

import javax.imageio.metadata.IIOMetadataNode;
import org.w3c.dom.Node;

public class NodeUtils {
    public static String createTextTree(Node root) {
        return NodeUtils.createTextTree(root, 0, true);
    }

    public static String createTextTree(Node root, int level) {
        return NodeUtils.createTextTree(root, level, true);
    }

    public static String createTextTree(Node root, int level, boolean addMargin) {
        int i;
        String res = root.getNodeName() + (root.getNodeValue() != null ? " - " + root.getNodeValue() : "");
        String prefix = "";
        for (i = 0; i < level; ++i) {
            prefix = addMargin ? prefix + "\u2502   " : prefix + "    ";
        }
        for (i = 0; i < root.getAttributes().getLength(); ++i) {
            if (i != root.getAttributes().getLength() - 1) {
                res = res + "\n" + prefix + "\u251c\u2500\u2500\u2500" + root.getAttributes().item(i).getNodeName();
                res = res + NodeUtils.createTextTree(root.getAttributes().item(i), level + 1);
                continue;
            }
            res = res + "\n" + prefix + "\u2514\u2500\u2500\u2500" + root.getAttributes().item(i).getNodeName();
            res = res + NodeUtils.createTextTree(root.getAttributes().item(i), level + 1, false);
        }
        for (i = 0; i < root.getChildNodes().getLength(); ++i) {
            if (i != root.getChildNodes().getLength() - 1) {
                res = res + "\n" + prefix + "\u251c\u2500\u2500\u2500" + root.getChildNodes().item(i).getNodeName();
                res = res + NodeUtils.createTextTree(root.getChildNodes().item(i), level + 1);
                continue;
            }
            res = res + "\n" + prefix + "\u2514\u2500\u2500\u2500" + root.getChildNodes().item(i).getNodeName();
            res = res + NodeUtils.createTextTree(root.getChildNodes().item(i), level + 1, false);
        }
        return res;
    }

    public static Node getNodeByName(String name, Node root) {
        for (int i = 0; i < root.getChildNodes().getLength(); ++i) {
            if (!root.getChildNodes().item(i).getNodeName().equals(name)) continue;
            return (IIOMetadataNode)root.getChildNodes().item(i);
        }
        IIOMetadataNode newNode = new IIOMetadataNode(name);
        root.appendChild(newNode);
        return newNode;
    }
}

