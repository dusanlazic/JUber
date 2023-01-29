from fastapi import FastAPI
from fastapi_utils.tasks import repeat_every
import simulate


simulate.get_drivers()


app = FastAPI()
sleep = 0.2


@app.get("/")
async def root():
    return {"message": "Hello World"}


@app.on_event("startup")
@repeat_every(seconds=sleep)
async def update():
    simulate.update_drivers()


@app.put("/sleep")
async def update_sleep(new_sleep: int = 1):
    sleep = new_sleep

