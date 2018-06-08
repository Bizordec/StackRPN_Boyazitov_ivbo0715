package Test;

import Lexer.*;
import RPN.*;
import Parser.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {

        File file = new File("lang");
        String myLang = new String(Files.readAllBytes(Paths.get(file.getPath())));

        System.out.println();
        Lexer lexer = new Lexer();
        List<Token> parserTokens = lexer.process(myLang);

        System.out.println("Lexer:");

        for (Token token : parserTokens) {
            System.out.println(token);
        }

        System.out.println();

        System.out.println("Source expression:");
        System.out.println(myLang);

        System.out.println();

        Parser parser = new Parser();
        System.out.println("Parser:");
        try {
            String parserResult = parser.parse(parserTokens);
            System.out.println(parserResult);
            if (parserResult.equals("Syntax is OK")) {
                ToRPN rpn = new ToRPN();

                System.out.println("\nRPN out: ");
                for (Token token: rpn.toRPN(parserTokens)){
                    System.out.print(token.getValue()+" ");
                }

                System.out.println();
                System.out.println();

                StackMachine stackMachine = new StackMachine();
                stackMachine.calculate(rpn.toRPN(parserTokens));
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
