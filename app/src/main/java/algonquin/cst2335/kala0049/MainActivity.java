package algonquin.cst2335.kala0049;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


/**
 * The MainActivity class represents the main activity of the application.
 * It allows users to input a password and checks its complexity.
 * @author Gurarman Singh
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    /** The TextView used to display messages.*/
    private TextView tv = null;
    /** The EditText used to input the password.*/
    private EditText etp = null;
    /** The Button used to perform the login action.*/
    private Button butn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.textView);
        etp = findViewById(R.id.editTextTextPassword);
        butn = findViewById(R.id.login_button);

        butn.setOnClickListener( (View v) ->{
            String password = etp.getText().toString();
            if (!checkPasswordComplexity(password)){
                tv.setText("You shall not pass!");
            }
            else{
                tv.setText("Your password meets the requirements");
            }
        });
    }

    /**
     * This function checks if the password entered by the user has an Upper Case letter,
     * a lower case letter, a number, and a special symbol. It shows a Toast message if otherwise
     *
     * @param passcheck string containing user password
     * @return True if all check passes and the password is complex enough and false if it fails.
     */
    boolean checkPasswordComplexity(String passcheck) {
        boolean foundUpperCase = false;
        boolean foundLowerCase = false;
        boolean foundNumber = false;
        boolean foundSpecial = false;

        for (int i = 0; i < passcheck.length(); i++) {
            char c = passcheck.charAt(i);

            if (Character.isUpperCase(c)) {
                foundUpperCase = true;
            } else if (Character.isLowerCase(c)) {
                foundLowerCase = true;
            } else if (Character.isDigit(c)) {
                foundNumber = true;
            } else if (isSpecialCharacter(c)) {
                foundSpecial = true;
            }
        }

        if (foundUpperCase && foundLowerCase && foundNumber && foundSpecial) {
            return true;
        } else {
            if (!foundUpperCase) {
                Toast.makeText(MainActivity.this, "The password is missing an uppercase letter", Toast.LENGTH_SHORT).show();
            }
            if (!foundLowerCase) {
                Toast.makeText(MainActivity.this, "The password is missing a lowercase letter", Toast.LENGTH_SHORT).show();
            }
            if (!foundNumber) {
                Toast.makeText(MainActivity.this, "The password is missing a number", Toast.LENGTH_SHORT).show();
            }
            if (!foundSpecial) {
                Toast.makeText(MainActivity.this, "The password is missing a symbol", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    }

    /**
     * Checks if the given character is a special character.
     *
     * @param c The character to check.
     * @return true if the character is a special character, false otherwise.
     */
    boolean isSpecialCharacter(char c) {
        switch (c) {
            case '#':
            case '?':
            case '*':
            case '!':
            case '@':
            case '$':
            case '&':
            case '%':
            case '^':
                return true;
            default:
                return false;
        }
    }

}