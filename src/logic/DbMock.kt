package logic

class DbException(text: String) : Exception(text)

class Db
{
    fun storeData(data: Data?) {
        if (Math.random() > 0.5) throw DbException("DB not accessible")
        println(data!!.value)
    }
}