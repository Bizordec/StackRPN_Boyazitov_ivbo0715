package RPN;

import Lexer.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ToRPN {
    private static Stack<Token> operators = new Stack<>();

    public static List<Token> toRPN(List<Token> tokens) {
        List<Token> output = new ArrayList<>();

        for (int i = 0; i < tokens.size(); i++) {
            switch (tokens.get(i).getType()) {
                case "INIT":
                    i = rpn_assign_expr(i, tokens, output);
                    break;
                case "VAR":
                    i = rpn_set_expr(i, tokens, output);
                    break;
                case "WHILE":
                    i = rpn_while_expr(i, tokens, output);
            }
        }
        return output;
    }

    private static int rpn_assign_expr(int index, List<Token> tokens, List<Token> output) {
        int subEnd = 0;
        for (int sub_i = index; sub_i < tokens.size(); sub_i++) {
            if (tokens.get(sub_i).getType().equals("EOL")) {
                subEnd = tokens.indexOf(tokens.get(sub_i)) + 1;
                break;
            }
        }
        List<Token> subList = tokens.subList(index, subEnd);
        label:
        for (Token token : subList) {
            switch (token.getType()) {
                case "VAR":
                case "NUM":
                    output.add(token);
                    break;
                case "LPAR":
                    operators.push(token);
                    break;
                case "RPAR":
                    while (!operators.peek().getType().equals("LPAR"))
                        output.add(operators.pop());
                    operators.pop();
                    break;
                case "MULDIV":
                case "ADDSUB":
                    if (!operators.empty()) {
                        while (token.getPriority() <= operators.peek().getPriority())
                            output.add(operators.pop());
                        operators.push(token);
                    } else
                        operators.push(token);
                    break;
                case "ASSIGN":
                    operators.push(new Token("NEWASSIGN", ":=", 1));
                    break;
                case "EOL":
                    while (!operators.empty())
                        output.add(operators.pop());
                    output.add(new Token("EOL", "EOL", 0));
                    break label;
            }
        }
        return subEnd - 1;
    }

    private static int rpn_set_expr(int index, List<Token> tokens, List<Token> output) {
        output.add(tokens.get(index));
        label1:
        for (int set_i = index + 1; set_i < tokens.size(); set_i++) {
            switch (tokens.get(set_i).getType()) {
                case "TYPE":
                    operators.push(tokens.get(set_i));
                    break;
                case "VAR":
                case "NUM":
                case "SET":
                    output.add(tokens.get(set_i));
                    break;
                case "ADD":
                    operators.push(tokens.get(set_i));
                    break;
                case "REMOVE":
                    operators.push(new Token("REMOVE", "rmv", 0));
                    break;
                case "CONTAINS":
                    operators.push(new Token("CONTAINS", "cont", 0));
                    break;
                case "EOL":
                    while (!operators.empty())
                        output.add(operators.pop());
                    output.add(new Token("EOL", "EOL", 0));
                    index = set_i;
                    break label1;
            }
        }
        return index;
    }

    private static int rpn_while_expr(int index, List<Token> tokens, List<Token> output) {
        int p0_index = 0;
        int begin_index = 0;
        label:
        for (int condition_i = index + 1; condition_i < tokens.size(); condition_i++) {
            switch (tokens.get(condition_i).getType()) {
                case "LPAR":
                    operators.push(tokens.get(condition_i));
                    break;
                case "VAR":
                case "NUM":
                    output.add(tokens.get(condition_i));
                    break;
                case "LOGOP":
                    operators.push(tokens.get(condition_i));
                    begin_index = output.size() - 1;
                    break;
                case "RPAR":
                    while (!operators.peek().getType().equals("LPAR"))
                        output.add(operators.pop());
                    operators.pop();
                    Token p0 = new Token("p", "p");
                    output.add(p0);
                    p0_index = output.lastIndexOf(p0);
                    output.add(new Token("!F", "!F"));
                    break;
                case "LBRACE":
                    label1:
                    for (int body_i = condition_i + 1; body_i < tokens.size(); body_i++) {
                        switch (tokens.get(body_i).getType()) {
                            case "WHILE":
                                body_i = rpn_while_expr(body_i, tokens, output);
                                break;
                            case "RBRACE":
                                Token p1 = new Token("p", "p");
                                output.add(p1);
                                int p1_index = output.lastIndexOf(p1);
                                output.get(p1_index).setValue(String.valueOf(begin_index));
                                output.add(new Token("!", "!"));
                                output.add(new Token("END", "END"));
                                int end_index = output.size() - 1;
                                output.get(p0_index).setValue(String.valueOf(end_index));
                                index = body_i;
                                break label1;
                            default:
                                body_i = rpn_assign_expr(body_i, tokens, output);
                                break;
                        }
                    }
                    break label;
            }
        }
        return index;
    }
}
