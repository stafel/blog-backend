# from https://intellitect.com/blog/opentelemetry-metrics-python/

import random

from fastapi import FastAPI
from fastapi.responses import HTMLResponse

app = FastAPI()


@app.get("/{name}/")
def get_naughty_or_nice(name: str):
    naughty_or_nice = "naughty" if bool(random.randint(0, 1)) else "nice"
    return HTMLResponse(f"{name}, you have been very {naughty_or_nice} this year!")
