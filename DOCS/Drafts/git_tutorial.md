#Git

## General User Commands
* `git pull` Pulls new commits on the current branch from the remote
    repository to the local repository (This will often result in explicit
    merges if your commits and the commits on remote have divereged)

* `git push` Pushs new commits on the current branch from the local
    repository to the remote repository (Perform a Pull before a push)

* `git checkout branch_name` Switchs context from the current branch to the
    branch with branch_name, you can use commit hashes as branch_name (It
    is best to commit changes on your current branch before switching)

* `git branch branch_name` Creates a branch with branch\_name.

* `git branch` Shows a list of branchs in the repository and also indicates
    your current branch

* `git commit -a -m "message"` Creates a commit of changes with the message
    describing the changes (Commits should be done often, after every logical
    piece of work I.E A new function or a single line bug fix)

* `git log` Displays details about commits in the repository with the newest
    first (You can also use shortlog instead of log).

## Admin Commands
Should generally only be used by QA or Project leaders to ensure that no
dangerous changes are made.

* `git branch -d branch_name` Deletes the branch with branch\_name

* `git revert`

* `git bisect` Performs a binary search between commits so you can determine
    where a bug was intoduced.



