package org.example;

import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class CurrencyConverterApp extends JFrame {
    private JTextField amountField;
    private JComboBox<String> fromCurrencyCombo;
    private JComboBox<String> toCurrencyCombo;
    private JLabel resultLabel;

    public CurrencyConverterApp() {
        setTitle("Currency Converter");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 150);
        setLayout(new GridLayout(4, 2, 5, 5));
        Color backgroundColor = new Color(87, 115, 136);
        Color textColor = new Color(60, 60, 60);
        Color buttonColor = new Color(130, 127, 79);
        Color resultColor = new Color(70, 130, 180);

        JLabel fromCurrencyLabel = new JLabel("Moneda a convertir:");
        fromCurrencyCombo = new JComboBox<>(getCurrencies());

        JLabel amountLabel = new JLabel("Monto:");
        amountField = new JTextField();

        JLabel toCurrencyLabel = new JLabel("Convertir en:");
        toCurrencyCombo = new JComboBox<>(getCurrencies());

        JButton convertButton = new JButton("Convierte");
        resultLabel = new JLabel("");

        convertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                convertCurrency();
            }
        });

        add(amountLabel);
        add(amountField);
        add(fromCurrencyLabel);
        add(fromCurrencyCombo);
        add(toCurrencyLabel);
        add(toCurrencyCombo);
        add(convertButton);
        add(resultLabel);
    }

    private String[] getCurrencies() {
        return new String[]{
                "MXN", "NZD", "QAR", "RUB", "USD", "AUD", "CNY", "GBP", "BRL","CAD", "EUR", "JPY",
        };
    }

    private void convertCurrency() {
        String apiKey = "56b1c478c165535c7b4ce464"; // Obtén tu API key gratuita en https://www.exchangerate-api.com/
        String baseCurrency = fromCurrencyCombo.getSelectedItem().toString();
        String targetCurrency = toCurrencyCombo.getSelectedItem().toString();
        double amountToConvert = Double.parseDouble(amountField.getText());

        double convertedAmount = CurrencyConverter.convertCurrency(apiKey, baseCurrency, targetCurrency, amountToConvert);

        if (convertedAmount != -1) {
            resultLabel.setText(amountToConvert + " " + baseCurrency + " = " + convertedAmount + " " + targetCurrency);
        } else {
            resultLabel.setText("Error en la conversión");
        }
    }


    public class CurrencyConverter {
        public static double convertCurrency(String apiKey, String baseCurrency, String targetCurrency, double amountToConvert) {
            try {
                URL url = new URL("https://v6.exchangerate-api.com/v6/" + apiKey + "/pair/" + baseCurrency + "/" + targetCurrency);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONObject jsonResponse = new JSONObject(response.toString());
                double exchangeRate = jsonResponse.getDouble("conversion_rate");

                return amountToConvert * exchangeRate;
            } catch (Exception e) {
                e.printStackTrace();
                return -1; // Si hay un error, retorna -1 como valor inválido.
            }

        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CurrencyConverterApp().setVisible(true);
            }
        });
    }
}
