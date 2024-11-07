package app.upryzing.crescent.api

class RevoltAPI {
    private val options: RevoltAPIOptions;

    constructor(
        options: RevoltAPIOptions
    ) {
        this.options = options;
    }

    constructor() {
        this.options = RevoltAPIOptions()
    }

    val session: Session = Session(this)
    val users: Users = Users(this)
    val http: Raw = Raw()
}