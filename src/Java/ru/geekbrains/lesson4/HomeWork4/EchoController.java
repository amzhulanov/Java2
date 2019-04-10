package Java.ru.geekbrains.lesson4.HomeWork4;

import static Java.ru.geekbrains.lesson4.HomeWork4.ViewWindow.textArea;

class EchoController implements ChatController {
    private ViewWindow viewer;

    public EchoController(ViewWindow viewer) {
        this.viewer = viewer;
    }

     @Override
     public void sendMessage(String message) {
         if (message.length() > 0) {
             textArea.append(message+ "\n");

         }
     }
 }
