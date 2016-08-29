package com.example.android.OrderGenerator;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends ActionBarActivity {

    int quantity = 0;
    int price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the preview button is clicked.
     */
    public void showOrder(View view) {
        CheckBox whippedCreamCheckBox = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        Boolean hasWhippedCream = whippedCreamCheckBox.isChecked();
        CheckBox chocolateCheckBox = (CheckBox) findViewById(R.id.chocolate_checkbox);
        Boolean hasChocolate = chocolateCheckBox.isChecked();
        EditText nameField = (EditText) findViewById(R.id.name_field);
        String name = nameField.getText().toString();

        price = calculatePrice(quantity, hasWhippedCream, hasChocolate);
        if (price >= 0)
        {
            String priceMessage = createOrderSummary(price, hasWhippedCream, hasChocolate, name);
            displayMessage(priceMessage);
        }
    }

    public void submitOrder(View view) {
        CheckBox whippedCreamCheckBox = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        Boolean hasWhippedCream = whippedCreamCheckBox.isChecked();
        CheckBox chocolateCheckBox = (CheckBox) findViewById(R.id.chocolate_checkbox);
        Boolean hasChocolate = chocolateCheckBox.isChecked();
        EditText nameField = (EditText) findViewById(R.id.name_field);
        String name = nameField.getText().toString();

        price = calculatePrice(quantity, hasWhippedCream, hasChocolate);
        if (price > 0)
        {
            String priceMessage = createOrderSummary(price, hasWhippedCream, hasChocolate, name);

            Intent intent = new Intent(Intent.ACTION_SENDTO);
            // Only email apps should handle this activity
            intent.setData(Uri.parse("mailto:"));
            intent.putExtra(Intent.EXTRA_SUBJECT, "Just Java order for " + name);
            intent.putExtra(Intent.EXTRA_TEXT, priceMessage);
            if(intent.resolveActivity(getPackageManager()) != null && price > 0) {
                startActivity(intent);
            }
        }
        if (price == 0) {
            Toast.makeText(this, "You haven't ordered", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This method is called when the increment button is clicked.
     */
    public void increment(View view) {
        if (quantity == 99) {
            // Show an error message
            Toast.makeText(this, "You can not order more than 99 cups of coffee a time", Toast.LENGTH_SHORT).show();
            return;
        }
        quantity = quantity + 1;
        display(quantity);
    }

    /**
     * This method is called when the decrement button is clicked.
     */
    public void decrement(View view) {
        if (quantity == 1)
        {
            // Show an error message
            Toast.makeText(this, "You can not have less than 1 cup of coffee", Toast.LENGTH_SHORT).show();
            return;
        }
        quantity = quantity - 1;
        display(quantity);
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void display(int number) {
        TextView quantityTextView = (TextView) findViewById(
                R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    /**
     * This method displays the given text on the screen.
     */
    private void displayMessage(String message) {
        TextView orderSummaryTextView = (TextView) findViewById(R.id.order_summary_text_view);
        orderSummaryTextView.setText(message);
    }

    /**
     * Calculates the price of the order.
     * @param addWhippedCream whether or not the user wants whipped cream topping
     * @param addChocolate whether or not the user wants chocolate topping
     * @return total price
     */
    private int calculatePrice(int quantity, boolean addWhippedCream, boolean addChocolate)
    {
        // Price of 1 cup of coffee
        int basePrice = 5;
        // Add $1 if user wants to add whipped cream topping
        if(addWhippedCream) {
            basePrice += 1;
        }
        // Add $2 is user wants to add chocolate topping
        if(addChocolate) {
            basePrice += 2;
        }
        // Calculate total price by multiplying by quantity
        price = quantity * basePrice;
        return price;
    }

    /**
     * Returns order summary given the quantity of the order.
     * @param price price per number of order
     * @param addWhippedCream whether or not user checked adding whipped cream option in Topping
     * @param addChocolate whether or not user checked adding chocolate option in Topping
     * @param name name the user entered in EditText View
     * @return order summary
     */
    private String createOrderSummary(int price, boolean addWhippedCream, boolean addChocolate, String name)
    {
        String priceMessage;
        if(price == 0) {
            Toast.makeText(this, "No quantity", Toast.LENGTH_SHORT).show();
            priceMessage = "$0";
        }
        else {
            priceMessage = "Name: " + name;
            priceMessage += "\nAdd whipped cream? " + addWhippedCream;
            priceMessage += "\nAdd chocolate? " + addChocolate;
            priceMessage += "\nQuantity: " + quantity;
            priceMessage += "\nTotal: $" + price;
            priceMessage += "\nThank you!";
        }
        return priceMessage;
    }
}
