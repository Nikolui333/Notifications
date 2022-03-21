package com.example.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import androidx.databinding.DataBindingUtil
import com.example.notifications.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val channelID = "com.example.notifications.channel1" //определяем id для канала уведомления
    private var notificationManager: NotificationManager? = null
    private var binding: ActivityMainBinding? = null
    private val KEY_REPLY = "key_reply"//ключ который будет отслеживать введенные данные

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager //Теперь, из функции onCreate, давайте вызовем функцию display, передав идентификатор, имя и описание.
        createNotificationChannel(channelID,"DemoChannel","this is a demo") //Мы создаем канал уведомлений, создавая экземпляр NotificationChannel, а затем передавая этот экземпляр к createNotificationChannel () класса NotificationManager. Давайте создадим новую функцию. частная забава createNotificationChannel Эта функция должна иметь 3 параметра. Идентификатор, имя и описание канала. Версия SDK должна быть android oreo или выше. Давайте напишем подтверждение для этого

        binding?.butOne?.setOnClickListener(this)

    }

    override fun onClick(view: View) {
        when(view.id) {
            R.id.butOne -> {
                displayNotification()
            }
        }
    }


    private fun displayNotification(){

        val notificationId = 7

        val startNotification = Intent(this, Info::class.java)

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this,
            0,
            startNotification,
            PendingIntent.FLAG_UPDATE_CURRENT
        )


        val remoteInput: RemoteInput = RemoteInput.Builder(KEY_REPLY).run{
            setLabel("Вы согласны сделать заказ?")
            build()
        }

        val replyAction : NotificationCompat.Action = NotificationCompat.Action.Builder(
            0,
            "Ответить",
            pendingIntent
        ).addRemoteInput(remoteInput)
            .build()

        val startDetails = Intent(this, Details::class.java)
        val pendingDetails: PendingIntent = PendingIntent.getActivity(
            this,
            0,
            startDetails,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val actionDetails : NotificationCompat.Action =
            NotificationCompat.Action.Builder(0,"Детали",pendingDetails).build()



        val startPrice = Intent(this, Price::class.java)
        val pendingPrice: PendingIntent = PendingIntent.getActivity(
            this,
            0,
            startPrice,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val actionPrice : NotificationCompat.Action =
            NotificationCompat.Action.Builder(0,"Стоимость",pendingPrice).build()




        val notification = NotificationCompat.Builder(this@MainActivity,channelID) //мы будем использовать NotificationCompat для создания объекта notification
            .setContentTitle("Новый Заказ")
            .setContentText("Поступил новый заказ!")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setAutoCancel(true)//автоотмена
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)//самый высокий приоритет
            .setContentIntent(pendingIntent)
            .addAction(actionDetails)
            .addAction(actionPrice)
            .addAction(replyAction)
            .build()
        notificationManager?.notify(notificationId,notification)//создание

    }

    private fun createNotificationChannel(id : String, name:String, channelDescription:String){//Вы можете видеть, что существуют различные уровни важности. Этот параметр определяет способ прерывания пользователя для любого уведомления, принадлежащего этому каналу. Давайте установим это как можно выше.
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(id,name,importance).apply {//нужно создать канал уведомлений, прежде чем публиковать какие-либо уведомления
                description = channelDescription
            }
            notificationManager?.createNotificationChannel(channel)

        }

    }
}