package app.upryzing.crescent.api

class RevoltAPI {
    internal val options: RevoltAPIOptions;

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
    val http: Raw = Raw(this)
}