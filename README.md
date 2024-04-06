# DS-assignment
Minecraft implementation goooo!!!

# Important commands
## Download code from GitHub (To clone)
1. There might be some code we want from GitHub. To clone the entire remote repository to the local repository. Go to the folder you want to store the code. Then, run
```sh
git clone <uri>
```
2. To explore other timelines (branches),
```sh
git switch <branch-name>
```
* By default, only master branch is downloaded in local repo.
## To update code in local repository from GitHub (To pull)
1. Let's say there is admin has updated the code in the master branch. First, you must make sure that you are in the "master" branch.
```sh
git branch -a
```
2. To change branch to master,
```sh
git switch master
```
3. To download code from GitHub (Same as updating the code),
```sh
git pull origin master
```

## To upload changes in local repository to GitHub (To push)
1. Let's say you added a folder containing images to the local repository (folder). You want to upload them to GitHub. First, check that you are in the correct branch.
```sh
git branch -a
```
2. It is always a good idea to check what files have been edited and/or not committed. You may repeat this command anytime.
```sh
git status
```
2. To flag a file for committing,
```sh
git add <file-name>
```
3. To flag all files for committing,
```sh
git add .
# Use this by default
```
4. To save your changes in local repository,
```sh
git commit -m "Initial commit I can write whatever I want bla bla"
```
5. To copy code from local repository to remote repository,
```sh
git push origin <branch-name>
```


# Reading Git Graphs
1. Reading git graphs allows us to understand the progress of our team/ your progress. First, navigate to the branch you are interested in.
```sh
git switch <branch-name>
```
2.  To view the branch,
```sh
git log --graph 
```
`HEAD` symbolises the point of time ("git commit") you are at 
`HEAD->master` means that you are at master branch
`commit adasd32we23...` is the unique id of the git commit
Every time, you run `git commit` a unique id is generated for that particular commit
Git graphs allows you to know who submitted code and when the code is submitted.
